package com.example.advancedtaskmanagement.task.task_progress;

import com.example.advancedtaskmanagement.task.TaskStatus;


public record TaskProgressRequestDto (
        Long taskId,
        TaskStatus status,
        String reason
) { }
