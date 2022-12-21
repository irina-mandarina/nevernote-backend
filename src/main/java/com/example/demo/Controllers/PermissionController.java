package com.example.demo.Controllers;

import com.example.demo.Services.PermissionService;
import com.example.demo.types.Method;
import com.example.demo.models.POST.PermissionRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@CrossOrigin("http://localhost:3000")
public class PermissionController {
    private final PermissionService permissionService;

    @PostMapping("/notes/{noteId}/permissions")
    ResponseEntity<String> addPermission(@RequestAttribute("username") String username,
                                         @PathVariable Long noteId, @RequestBody PermissionRequest permissionRequest) {

        ResponseEntity<String> response = permissionService.grantPermission(username, permissionRequest);
//        logService.log(response, ownerUsername, Method.POST, "/notes/" + noteId + "/permissions");
        return response;
    }

    @DeleteMapping("/notes/{noteId}/permissions/{permission}")
    ResponseEntity<String> deletePermission(@RequestAttribute("username") String username,
                                            @PathVariable Long noteId,
                                            @RequestBody PermissionRequest permissionRequest,
                                            @PathVariable("permission") Method permissionType) {
        permissionRequest.setNoteId(noteId); // just in case
        permissionRequest.setPermissionType(permissionType);

        ResponseEntity<String> response = permissionService.deletePermission(username, permissionRequest);
//        logService.log(response, ownerUsername, Method.DELETE, "/notes/" + noteId + "/permissions/" + permissionType.name);
        return response;
    }

    @GetMapping("/notes/{noteId}/permissions")
    ResponseEntity<String> getPermissions(@RequestAttribute("username") String username,
                                          @PathVariable Long noteId) {
//        if (ownerUsername.isEmpty()) {
//            ResponseEntity<String> response = permissionService.getAllPermissions(noteId);
//        }
        ResponseEntity<String> response = permissionService.getAllPermissionsForNote(username, noteId);
//        logService.log(response, logInRequest.getUsername(), Method.GET, "/notes/" + noteId + "/permissions");
        return response;
    }
}
