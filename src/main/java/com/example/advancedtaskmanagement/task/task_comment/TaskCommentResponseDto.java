package com.example.advancedtaskmanagement.task.task_comment;

import java.time.LocalDateTime;
import java.util.Date;


public record TaskCommentResponseDto (
        Long id,
        String content,
        LocalDateTime createdAt,
        String userName
) { }
