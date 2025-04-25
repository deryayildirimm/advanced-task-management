package com.example.advancedtaskmanagement.task.task_attachment;

import java.time.LocalDateTime;

public record TaskAttachmentResponseDto (
        Long id,
        String filePath,
        String fileName,
        LocalDateTime uploadedAt
){ }
