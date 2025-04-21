package com.example.advancedtaskmanagement.task;


public record TaskRequestDto (
        String description,
        TaskPriority priority,
        TaskStatus status,
        String title,
        String acceptanceCriteria,
        Long assignedUserId
) { }
