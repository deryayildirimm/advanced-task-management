package com.example.advancedtaskmanagement.security;

import com.example.advancedtaskmanagement.user.Role;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class RegisterRequest {

    private String name;
    private String username;
    private String password;
    private Set<Role> roles;

}
