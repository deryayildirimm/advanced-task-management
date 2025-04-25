package com.example.advancedtaskmanagement.project.project_user_assignment;

import org.springframework.stereotype.Component;

@Component
public class ProjectUserAssignmentMapper {

    public ProjectUserAssignmentResponseDto toDto(ProjectUserAssignment projectUserAssignment) {

        return new ProjectUserAssignmentResponseDto(
                projectUserAssignment.getId(),
                projectUserAssignment.getProject().getTitle(),
                projectUserAssignment.getUser().getName(),
                projectUserAssignment.getRole()
        );
    }

}
