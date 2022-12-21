package com.example.demo.Services;

import com.example.demo.Entities.Permission;
import com.example.demo.models.POST.PermissionRequest;
import com.example.demo.types.Method;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface PermissionService {
    ResponseEntity<String> grantPermission(String username, PermissionRequest permissionRequest);
    ResponseEntity<String> deletePermission(String username, PermissionRequest permissionRequest);
    List<Permission> findAllByUserUsernameAndNoteId(String username, Long noteId);
    Permission findPermissionByNoteIdAndUserUsernameAndPermissionType(Long noteId, String username, Method permissionType);
//    ResponseEntity<String> getMyPermissions(String username, Long noteId);

    ResponseEntity<String> getAllPermissionsForNote(String username, Long noteId);
    List<Permission> findPermissionsByNoteId(Long noteId);
}
