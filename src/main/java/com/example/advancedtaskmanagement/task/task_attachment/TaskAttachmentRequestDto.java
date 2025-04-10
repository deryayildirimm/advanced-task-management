package com.example.advancedtaskmanagement.task.task_attachment;


public record TaskAttachmentRequestDto(
        Long taskId,
        String filePath
) { }
