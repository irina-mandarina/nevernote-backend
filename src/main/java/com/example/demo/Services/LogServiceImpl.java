package com.example.demo.Services;

import com.example.demo.Entities.Log;
import com.example.demo.Entities.User;
import com.example.demo.Repositories.LogsRepository;
import com.example.demo.models.GET.LogResponse;
import com.example.demo.models.GET.NoteResponse;
import com.example.demo.types.Method;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;

@Service
@RequiredArgsConstructor
public class LogServiceImpl implements LogService {
    private final LogsRepository logsRepository;
    private final NoteService noteService;
    private final UserService userService;
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

    @Override
    public ResponseEntity<String> getUserLogs(String username) {
        User user = userService.findByUsername(username);
        if (Objects.isNull(user)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        List<LogResponse> response = logsToLogResponses(findAllByUserOrderByIdDesc(user));

        Gson gson = new Gson();
        return ResponseEntity.status(HttpStatus.OK)
                .body(gson.toJson( response ));
    }

    @Override
    public List<Log> findAllByUserOrderByIdDesc(User user) {
        return logsRepository.findAllByUserOrderByIdDesc(user);
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
    public void log(ResponseEntity<String> response, String username, Method methodType, String path) {
        if (!Arrays.asList(200, 201, 204).contains(response.getStatusCode().value())) {
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
        }
        else if (path.contains("bio")) {
            log.setSubject(username);
            subjectParam = "'s bio";
        }
        else if (path.contains("details")) {
            log.setSubject(username);
            subjectParam = "'s details";
        }
        else if (path.contains("register")) {
            log.setSubject("registered");
        }
        else if (path.contains("login")) {
            log.setSubject("logged in");
        }
        else if (path.contains("logout")) {
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


}
