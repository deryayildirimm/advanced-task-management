package com.example.advancedtaskmanagement.project;

import org.springframework.stereotype.Component;

@Component
public class ProjectMapper {

    public ProjectResponseDto toResponseDto(Project project) {

        return new ProjectResponseDto(
                project.getId(),
                project.getTitle(),
                project.getDescription(),
                project.getStartDate(),
                project.getEndDate(),
                project.getStatus(),
                project.getDepartment() != null ? project.getDepartment().getName() : null
        );

    }

}
