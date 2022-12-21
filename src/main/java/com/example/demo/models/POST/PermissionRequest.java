package com.example.demo.models.POST;

import com.example.demo.Entities.Permission;
import com.example.demo.types.Method;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class PermissionRequest {
    Timestamp grantedTimestamp;
    String username;
    Method permissionType;
    Long noteId;

    public PermissionRequest(String username, Method permissionType, Long noteId) {
        this.username = username;
        this.noteId = noteId;
        this.permissionType = permissionType;
    }

    public PermissionRequest(Permission permission) {
        this.permissionType = permission.getPermissionType();
        this.noteId = permission.getNote().getId();
        this.username = permission.getUser().getUsername();
        this.grantedTimestamp = permission.getGrantedTimestamp();
    }
}
