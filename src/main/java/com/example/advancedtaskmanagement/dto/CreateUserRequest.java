package com.example.advancedtaskmanagement.dto;

import com.example.advancedtaskmanagement.model.Role;
import lombok.Builder;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
@Builder
public class CreateUserRequest {

    String name;
    String username;
    String password;
    Set<Role> roles = new HashSet<>();
}
