package com.example.advancedtaskmanagement.task.task_progress;

import com.example.advancedtaskmanagement.task.TaskStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;


public record TaskProgressRequestDto (
        @NotNull(message = "Task ID is required")
        Long taskId,
        @NotNull(message = "Status is required")
        TaskStatus status,
        @Size(max = 500, message = "Reason must be less than 500 characters")
        String reason
) { }
