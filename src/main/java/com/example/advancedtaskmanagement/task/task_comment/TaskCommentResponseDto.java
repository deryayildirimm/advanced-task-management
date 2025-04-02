package com.example.advancedtaskmanagement.task.task_comment;

import java.util.Date;


public record TaskCommentResponseDto (
        Long id,
        String content,
        Date createdAt,
        Long userId
) { }
