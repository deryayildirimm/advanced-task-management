package com.example.advancedtaskmanagement.task.task_attachment;


import jakarta.validation.constraints.NotNull;

public record TaskAttachmentRequestDto(
        @NotNull(message = "Task ID is required")
        Long taskId,
        String filePath
) { }
