package com.example.advancedtaskmanagement.project.project_user_assignment;

import com.example.advancedtaskmanagement.user.Role;

public record ProjectUserAssignmentRequestDto(
        Long projectId,
        Long userId,
        Role role
) { }
