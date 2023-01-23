package com.example.demo.services;

import com.example.demo.Entities.Log;
import com.example.demo.Entities.User;
import com.example.demo.repositories.LogsRepository;
import com.example.demo.models.GET.LogResponse;
import com.example.demo.models.GET.NoteResponse;
import com.example.demo.repositories.search_criteria.OrderCriteria;
import com.example.demo.repositories.search_criteria.SearchCriteria;
import com.example.demo.types.AuthorityType;
import com.example.demo.types.Method;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.example.demo.repositories.search_criteria.consumers.LogSearchQueryCriteriaConsumer;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import javax.persistence.EntityManager;
import java.sql.Timestamp;
import java.util.*;

@Service
@RequiredArgsConstructor
public class LogServiceImpl implements LogService {
    private final LogsRepository logsRepository;
    private final NoteService noteService;
    private final UserService userService;
    private final AuthorityService authorityService;

    @Override
    public ResponseEntity<String> getNoteLogs(Long noteId) {
        if (Objects.isNull(noteService.findNoteById(noteId))) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        List<LogResponse> response = logsToLogResponses(findAllBySubjectAndSubjectId("NOTE", noteId));

        Gson gson = new Gson();
        return ResponseEntity.status(HttpStatus.OK)
                .body(gson.toJson( response ));
    }
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public ResponseEntity<String> searchLogs(String username, final List<SearchCriteria> params, List<OrderCriteria> orderParams) {
        final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        final CriteriaQuery<Log> query = builder.createQuery(Log.class);
        final Root<Log> r = query.from(Log.class);

        Predicate predicate = builder.conjunction();
        for (SearchCriteria param: params) {
            if (param.getKey().equals("username")) {
                // Add a predicate to filter by user id and not username
                try {
                    predicate = builder.and(predicate, builder.equal(r.get("user"),
                            userService.findByUsername((String) param.getValue()).getId())
                    );
                }
                catch (NullPointerException e) {
                    // no such user
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
                }
                    // replace the param
                params.set(params.indexOf(param), new SearchCriteria(
                        "user", param.getOperation(), userService.findByUsername((String) param.getValue()).getId()
                ));
            }
        }
        // for users without an ADMIN role, show only their own logs and the logs for their notes
        if (!authorityService.hasRole(username, AuthorityType.ADMIN)) {
            User user = userService.findByUsername(username);
            Predicate userPredicate = builder.equal( r.get("user"), user );
            Predicate notePredicate = builder.equal(r.get("subject"), "note");

            List<Long> ids = noteService.findNotesByUser(user);
            Predicate noteIdPredicate = r.get("subjectId").in(ids);
            notePredicate = builder.and(notePredicate, noteIdPredicate);

            predicate = builder.and(predicate, builder.or(userPredicate, notePredicate));
        }
        LogSearchQueryCriteriaConsumer searchConsumer = new LogSearchQueryCriteriaConsumer(predicate, builder, r);
        params.stream().forEach(searchConsumer);
        predicate = searchConsumer.getPredicate();
        query.where(predicate);

        List<Order> order = new ArrayList<>();
        for (OrderCriteria param: orderParams) {
            if (param.getDesc()) {
                order.add(builder.desc(r.get(param.getKey())));
            }
            else {
                order.add(builder.asc(r.get(param.getKey())));
            }
        }
        query.orderBy(order);

        List<LogResponse> response = logsToLogResponses(
                entityManager.createQuery(query).getResultList()
        );

        return ResponseEntity.ok().body(
                (new Gson()).toJson(
                        response
                )
        );
    }

    @Override
    public ResponseEntity<String> getUserLogs(String username) {
        User user = userService.findByUsername(username);
        if (Objects.isNull(user)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        List<LogResponse> response = logsToLogResponses(
                findAllByUserOrSubjectAndSubjectIdIsInOrderByIdDesc(
                        user, "note", noteService.findNotesByUser(user)
                )
        );

        Gson gson = new Gson();
        return ResponseEntity.status(HttpStatus.OK)
                .body(gson.toJson( response ));
    }

    @Override
    public List<Log> findAllByUserOrSubjectAndSubjectIdIsInOrderByIdDesc(
            User user, String subject, List<Long> subjectIds) {
        return logsRepository.findAllByUserOrSubjectAndSubjectIdIsInOrderByIdDesc(user, subject, subjectIds);
    }

    @Override
    public List<Log> findAllBySubjectAndSubjectId(String subject, Long subjectId) {
        return logsRepository.findAllBySubjectAndSubjectId(subject, subjectId);
    }

    @Override
    public List<LogResponse> logsToLogResponses(List<Log> logs) {
        List<LogResponse> response = new ArrayList<>(logs.size());
        for (Log log: logs) {
            response.add(new LogResponse(log));
        }
        return response;
    }

    @Override
    public void log(ResponseEntity<String> response, String username,
                    Method methodType, String path) {
        // if the request is not successful, don't log it
        if (!Arrays.asList(200, 201, 204).contains(response.getStatusCode().value())) {
            return;
        }
        if (path.contains("permissions")) {
            logPermission(username, methodType, path);
            return;
        }
        else if (path.contains("roles")) {
            logRoles(username, methodType, path);
            return;
        }
        else if (path.contains("auth")) {
            logAuth(username, methodType, path);
            return;
        }
        else if (path.contains("notes")) {
            logNotes(response, username, methodType, path);
            return;
        }
        Log log = new Log();
        log.setUser(userService.findByUsername(username));
        log.setPath(path);
        log.setMethod(methodType);
        log.setTimestamp(new Timestamp(new Date().getTime()));
        String message = username + " ";
        String subjectParam = "";
        if (path.contains("/notes")) {
            log.setSubject("notes");
            if ((methodType.equals(Method.POST)) || (methodType.equals(Method.PUT))) {
                log.setSubject("note");
                if (path.contains("/notes/")) {
                    subjectParam =
                            // add the last part of the path for example "privacy"
                            "'s " + path.split("/")[path.split("/").length - 1];
                    // check if subject param is int
                    try {
                        Integer.parseInt(path.split("/")[path.split("/").length - 1]);
                        subjectParam = "'s content";
                        System.out.printf(subjectParam);
                    } catch (Exception e) {
                        // it's ok if it's not an int
                    }
                }
                Gson gson = new Gson();
                NoteResponse noteResponse = gson.fromJson(response.getBody(), NoteResponse.class);
                log.setSubjectId(noteResponse.getId());
            }
        } else if (path.contains("bio")) {
            log.setSubject(username);
            subjectParam = "'s bio";
        } else if (path.contains("details")) {
            log.setSubject(username);
            subjectParam = "'s details";
        } else if (path.contains("register")) {
            log.setSubject("registered");
        } else if (path.contains("login")) {
            log.setSubject("logged in");
        } else if (path.contains("logout")) {
            log.setSubject("logged out");
        }

        switch (methodType) {
            case GET -> message += "retrieved ";
            case PUT -> message += "changed ";
            case POST -> {
                if (!path.contains("auth")) {
                    message += "created ";
                }
            }
            case DELETE -> message += "deleted ";
        }

        message += log.getSubject(); // ex. note
        if (!Objects.isNull(log.getSubjectId())) { // ex. 125
            message += " " + log.getSubjectId();
        }

        message += subjectParam;


        log.setMessage(message + ".");
        logsRepository.save(log);

    }



    private void logNotes(ResponseEntity<String> response, String username, Method methodType, String path) {
        Log log = new Log();
        log.setPath(path);
        log.setUser(userService.findByUsername(username));
        log.setMethod(methodType);
        log.setTimestamp(new Timestamp(new Date().getTime()));
        String message = username + " ";
        if (methodType.equals(Method.GET)) {
            message += "retrieved " + path.substring(path.indexOf('?') + 1) + " notes";
        }
        else {
            log.setSubject("note");

            Gson gson = new Gson();
            NoteResponse noteResponse = gson.fromJson(response.getBody(), NoteResponse.class);
            log.setSubjectId(noteResponse.getId());

            if (methodType.equals(Method.POST)) {
                message += "created note " + noteResponse.getId();
            }
            else if (methodType.equals(Method.DELETE)) {
                message += "deleted note " + noteResponse.getId();
            }
            else if (methodType.equals(Method.PUT)) {
                if ((path.split("/")[path.split("/").length - 1]).equals("completed")) {
                    message += "completed note " + noteResponse.getId();
                }
                else if ((path.split("/")[path.split("/").length - 1]).equals("privacy")) {
                    message += "changed note " + noteResponse.getId() + "'s privacy";
                }
                else {
                    message += "changed note " + noteResponse.getId() + "'s privacy";
                }
            }
        }
        log.setMessage(message + ".");
        logsRepository.save(log);
    }

    private void logPermission(String username,
                               Method methodType, String path) {
        Log log = new Log();
        log.setPath(path);
        log.setUser(userService.findByUsername(username));
        log.setMethod(methodType);
        log.setTimestamp(new Timestamp(new Date().getTime()));

        String message = username + " ";

        // get the note id from path because it's easier and using gson is unnecessary
        Long noteId = Long.valueOf(path.split("/")[2]);
        String permittedUsername = path
                .replace("?", "/")
                .split("/")
                [path.replace("?", "/").split("/").length - 1] ;
        String permissionType = path
                .replace("?", "/")
                .split("/")
                [path.replace("?", "/").split("/").length - 2];

        if (methodType.equals(Method.GET)) {
            message += "retrieved ";
            message += "note " + noteId + " permissions";
        }
        else if (methodType.equals(Method.DELETE)) {
            message += "removed ";
            message += permittedUsername + "'s " + permissionType
                    + " permission for " + "note " + noteId;
        }
        else if (methodType.equals(Method.POST)) {
            message += "granted ";
            message += permittedUsername + " a " + permissionType
                    + " permission for " + "note " + noteId;
        }
        log.setSubjectId(noteId);
        log.setSubject("note permissions");
        log.setMessage(message + ".");
        logsRepository.save(log);
    }

    private void logRoles(String username,
                          Method methodType, String path) {
        Log log = new Log();
        log.setUser(userService.findByUsername(username));
        log.setPath(path);
        log.setTimestamp(new Timestamp(new Date().getTime()));
        log.setSubjectId(userService.findByUsername(username).getId());
        log.setMethod(methodType);

        String message = username + " ";
        if (methodType.equals(Method.GET)) {
            message += "retrieved ";
            if (path.contains("auth")) {
                log.setSubject(username + " roles");
                message += "their roles.";
            }
            else {
                log.setSubject("roles");
                message += "all roles.";
            }
        }
        else {
            String roleType = path.substring(path.indexOf('?')).replace("?", "");
            if (methodType.equals(Method.DELETE)) {
                message += "removed " + path.split("/")[2] + " " + roleType + " role.";
            }
            if (methodType.equals(Method.POST)) {
                message += "gave " + path.split("/")[2] + " a/an " + roleType + " role.";
            }
        }

        log.setMessage(message);
        logsRepository.save(log);
    }

    private void logAuth(String username,
                         Method methodType, String path) {
        Log log = new Log();
        log.setUser(userService.findByUsername(username));
        log.setTimestamp(new Timestamp(new Date().getTime()));
        log.setPath(path);
        log.setMethod(methodType);

        String message = username + " ";
        if (path.contains("register")) {
            message  += ("registered");
            log.setSubject(username + " registration");
        } else if (path.contains("login")) {
            message += ("logged in");
            log.setSubject(username + " login");
        } else if (path.contains("logout")) {
            message += ("logged out");
            log.setSubject(username + " logout");
        }
        log.setMessage(message);

        log.setSubjectId(userService.findByUsername(username).getId());

        logsRepository.save(log);
    }
}
