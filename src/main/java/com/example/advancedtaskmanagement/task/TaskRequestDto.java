package com.example.advancedtaskmanagement.task;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TaskRequestDto (

        @NotBlank(message = "Description must not be blank")
        String description,
        @NotNull(message = "Priority must be provided")
        TaskPriority priority,
        @NotNull(message = "Status must be provided")
        TaskStatus status,
        @NotBlank(message = "Title must not be blank")
        String title,
        String acceptanceCriteria,
        @NotNull(message = "Assigned user ID is required")
        Long assignedUserId
) { }
