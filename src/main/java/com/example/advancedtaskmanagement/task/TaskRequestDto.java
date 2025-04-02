package com.example.advancedtaskmanagement.task;


import com.example.advancedtaskmanagement.project.Project;
import lombok.AllArgsConstructor;
import lombok.Getter;

public record TaskRequestDto (
        String description,
        TaskPriority priority,
        TaskStatus status,
        String title,
        Project project,
        String acceptanceCriteria,
        Long assignedUserId
) { }
