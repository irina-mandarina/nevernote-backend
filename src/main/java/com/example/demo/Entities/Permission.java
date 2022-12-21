package com.example.demo.Entities;

import com.example.demo.types.Method;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

@Entity
@Getter
@Setter
@Table(name = "permissions")
public class Permission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "note_id", referencedColumnName = "id")
    @NotNull
    private Note note;

    // the reciever of the permission
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @NotNull
    private User user;

    // the owner who granted the permission
//    @ManyToOne
//    @JoinColumn(name = "owner_id", referencedColumnName = "id")
//    @NotNull
//    private User owner;

    @Column(name = "permission_type")
    @NotNull
    private Method permissionType;

    @Column(name = "granted_timestamp")
    @NotNull
    private Timestamp grantedTimestamp;

}
