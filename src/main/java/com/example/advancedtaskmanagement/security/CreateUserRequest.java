package com.example.advancedtaskmanagement.security;

import com.example.advancedtaskmanagement.user.Role;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class CreateUserRequest {

    String name;
    String username;
    String password;
    Set<Role> roles = new HashSet<>();
}
