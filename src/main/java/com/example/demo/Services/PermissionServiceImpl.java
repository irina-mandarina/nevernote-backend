package com.example.demo.Services;

import com.example.demo.Entities.Permission;
import com.example.demo.Entities.Note;
import com.example.demo.Entities.User;
import com.example.demo.Repositories.PermissionRepository;
import com.example.demo.models.POST.PermissionRequest;
import com.example.demo.types.Method;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class PermissionServiceImpl implements PermissionService {
    private final PermissionRepository permissionRepository;
    private final UserService userService;
    private final NoteService noteService;

    @Override
    public ResponseEntity<String> grantPermission(String username, PermissionRequest permissionRequest) {
        List<Permission> permissions = findAllByUserUsernameAndNoteId(permissionRequest.getUsername(), permissionRequest.getNoteId());
        Gson gson = new Gson();
        for (Permission permission: permissions) {
            if (permission.getPermissionType().equals( permissionRequest.getPermissionType() )) {
                // permission already exists
                return ResponseEntity.status(HttpStatus.CREATED).body(
                        gson.toJson(
                            new PermissionRequest(permission)
                        )
                );
            }
        }
        User granter = userService.findByUsername(username);
        User user = userService.findByUsername(permissionRequest.getUsername());
        Note note = noteService.findNoteById(permissionRequest.getNoteId());

        if (Objects.isNull(user) || Objects.isNull(note)) {
            // username or note does not exist
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

//        if (!note.getUser().getUsername().equals(owner.getUsername())) {
//            // owner is not the owner of the note
//            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
//        }

        Permission permission = new Permission();
        permission.setPermissionType(permissionRequest.getPermissionType());
        permission.setUser(user);
        permission.setNote(note);
        permission.setGrantedTimestamp(new Timestamp(new Date().getTime()));

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        gson.toJson(
                                new PermissionRequest(permissionRepository.save(permission))
                        )
                );
    }

    @Override
    public ResponseEntity<String> deletePermission(String username, PermissionRequest permissionRequest) {
        List<Permission> permissions = findAllByUserUsernameAndNoteId(permissionRequest.getUsername(), permissionRequest.getNoteId());
        Gson gson = new Gson();
        User granter = userService.findByUsername(permissionRequest.getOwnerUsername());
        User user = userService.findByUsername(permissionRequest.getUsername()); // the one who receives the permission
        Note note = noteService.findNoteById(permissionRequest.getNoteId());

        if (Objects.isNull(user) || Objects.isNull(note)) {
            // username or note does not exist
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        if (!note.getUser().getUsername().equals(granter.getUsername()) || user.equals(granter)) {
            // granter is not the owner of the note
            // or the owner is trying to remove his own permissions
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }

        for (Permission permission: permissions) {
            if (permission.getPermissionType().equals( permissionRequest.getPermissionType() )) {
                // permission exists
                permissionRepository.delete(permission);
                return ResponseEntity.status(HttpStatus.OK).body(null);
            }
        }
        return ResponseEntity.status(HttpStatus.OK)
                .body(null);
    }

    @Override
    public ResponseEntity<String> getAllPermissionsForNote(String username, Long noteId) {
        Gson gson = new Gson();
        List<Permission> permissions = findPermissionsByNoteId(noteId);
        // if the owner requests getting the permissions
        if (noteService.findNoteById(noteId).getUser().getUsername().equals(username)) {
            // the owner should have all permissions
            if (permissions.size() != 4) {
                // there are missing permissions in the db
                List<Method> existingPermissionTypes = new ArrayList<>(4);
                for (Permission permission: permissions) {
                    switch (permission.getPermissionType()) {
                        case GET -> existingPermissionTypes.add(Method.GET);
                        case PUT -> existingPermissionTypes.add(Method.PUT);
                        case POST -> existingPermissionTypes.add(Method.POST);
                        case DELETE -> existingPermissionTypes.add(Method.DELETE);
                    }
                }
                // grant missing permissions
                if (!existingPermissionTypes.contains(Method.POST)) {
                    grantPermission(username, new PermissionRequest(username, username, Method.POST, noteId));
                }
                if (!existingPermissionTypes.contains(Method.GET)) {
                    grantPermission(username, new PermissionRequest(username, username, Method.GET, noteId));
                }
                if (!existingPermissionTypes.contains(Method.PUT)) {
                    grantPermission(username, new PermissionRequest(username, username, Method.PUT, noteId));
                }
                if (!existingPermissionTypes.contains(Method.DELETE)) {
                    grantPermission(username, new PermissionRequest(username, username, Method.DELETE, noteId));
                }

                // if the owner of the note requests the permissions, he should get all of them
                List<PermissionRequest> permissionRequestList = new ArrayList<>(4);
                for (Permission permission: findPermissionsByNoteId(noteId)) {
                    permissionRequestList.add(new PermissionRequest(permission));
                }

                return ResponseEntity.status(HttpStatus.OK)
                        .body(
                                gson.toJson(
                                        permissionRequestList
                                )
                        );
            }
            // if another user requests the permissions for a note, he should get only his
            List<PermissionRequest> permissionRequestList = new ArrayList<>(4);
            for (Permission permission: findAllByUserUsernameAndNoteId(username, noteId)) {
                permissionRequestList.add(new PermissionRequest(permission));
            }

            return ResponseEntity.status(HttpStatus.OK)
                    .body(
                        gson.toJson(
                                permissionRequestList
                        )
            );
        }

        return null;
    }

    @Override
    public List<Permission> findPermissionsByNoteId(Long noteId) {
        return permissionRepository.findPermissionsByNoteId(noteId);
    }

    @Override
    public List<Permission> findAllByUserUsernameAndNoteId(String username, Long noteId) {
        return permissionRepository.findAllByUserUsernameAndNoteId(username, noteId);
    }

    @Override
    public Permission findPermissionByNoteIdAndUserUsernameAndPermissionType(Long noteId, String username, Method permissionType) {
        return permissionRepository.findPermissionByNoteIdAndUserUsernameAndPermissionType(noteId, username, permissionType);
    }

}
