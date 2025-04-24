package com.example.advancedtaskmanagement.project.project_user_assignment;

import com.example.advancedtaskmanagement.user.Role;
import jakarta.validation.constraints.NotNull;

public record ProjectUserAssignmentRequestDto(
        @NotNull(message = "Project ID must not be null")
        Long projectId,
        @NotNull(message = "User ID must not be null")
        Long userId,
        @NotNull(message = "Role must be provided")
        Role role
) { }
