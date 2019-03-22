package com.dubhad.qaproject.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class User extends Entity {
    private long id;
    private String username;
    private String email;
    private String passwordSalt;
    private String password;
    private UserRole role;
    private UserStatus status;
    private String avatarPath;
}
