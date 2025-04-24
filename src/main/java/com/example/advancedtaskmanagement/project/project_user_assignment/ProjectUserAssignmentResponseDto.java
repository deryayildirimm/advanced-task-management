package com.example.advancedtaskmanagement.project.project_user_assignment;

import com.example.advancedtaskmanagement.user.Role;


public record ProjectUserAssignmentResponseDto(
        Long id,
        String projectTitle,
        String userName,
        Role role

) {
}
