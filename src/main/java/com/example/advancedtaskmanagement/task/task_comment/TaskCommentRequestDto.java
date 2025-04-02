package com.example.advancedtaskmanagement.task.task_comment;


public record TaskCommentRequestDto(
        Long taskId,
        Long userId,
        String comment
) { }
