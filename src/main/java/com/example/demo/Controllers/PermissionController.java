package com.example.demo.Controllers;

import com.example.demo.Services.LogService;
import com.example.demo.Services.PermissionService;
import com.example.demo.types.Method;
import com.example.demo.models.POST.PermissionRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@CrossOrigin("http://localhost:3000")
public class PermissionController {
    private final PermissionService permissionService;
    private final LogService logService;

    @PostMapping("/notes/{noteId}/permissions/{permission}")
    ResponseEntity<String> addPermission(@RequestAttribute("username") String username,
                                         @PathVariable Long noteId, @RequestParam("username") String permittedUsername,
                                         @PathVariable("permission") Method permissionType) {

        ResponseEntity<String> response = permissionService.grantPermission(username, new PermissionRequest(permittedUsername, permissionType, noteId));
        logService.log(response, username, Method.POST, "/notes/" + noteId + "/permissions/" + permissionType.name() + "?" + permittedUsername);
        return response;
    }

    @DeleteMapping("/notes/{noteId}/permissions/{permission}")
    ResponseEntity<String> deletePermission(@RequestAttribute("username") String username,
                                            @PathVariable Long noteId,
                                            @RequestParam("username") String permittedUsername,
                                            @PathVariable("permission") Method permissionType) {
        PermissionRequest permissionRequest = new PermissionRequest();
        permissionRequest.setNoteId(noteId);
        permissionRequest.setPermissionType(permissionType);
        permissionRequest.setUsername(permittedUsername);

        ResponseEntity<String> response = permissionService.deletePermission(username, permissionRequest);
        logService.log(response, username, Method.DELETE, "/notes/" + noteId + "/permissions/" + permissionType.name() + "?" + permittedUsername);
        return response;
    }

    @GetMapping("/notes/{noteId}/permissions")
    ResponseEntity<String> getPermissions(@RequestAttribute("username") String username,
                                          @PathVariable Long noteId) {
        ResponseEntity<String> response = permissionService.getAllPermissionsForNote(username, noteId);
        logService.log(response, username, Method.GET, "/notes/" + noteId + "/permissions");
        return response;
    }
}
