package com.example.demo.repositories;

import com.example.demo.Entities.Permission;
import com.example.demo.types.Method;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PermissionRepository extends JpaRepository<Permission, Long> {
    List<Permission> findAllByUserUsernameAndNoteId(String username, Long noteId);
    Permission findPermissionByNoteIdAndUserUsernameAndPermissionType(Long noteId, String username, Method permissionType);
    List<Permission> findPermissionsByNoteId(Long noteId);
}
