package com.example.advancedtaskmanagement.task.task_progress;

import com.example.advancedtaskmanagement.task.TaskStatus;

import java.time.LocalDateTime;
import java.util.Date;

public record TaskProgressResponseDto (
        Long id,
        TaskStatus status,
        String reason,
        LocalDateTime changedAt
) { }
