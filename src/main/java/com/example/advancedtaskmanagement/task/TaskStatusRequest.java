package com.example.advancedtaskmanagement.task;

public record TaskStatusRequest(
        TaskStatus status,
        String reason
) {
}
