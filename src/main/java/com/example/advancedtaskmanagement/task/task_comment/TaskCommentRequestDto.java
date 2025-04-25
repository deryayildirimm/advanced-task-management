package com.example.advancedtaskmanagement.task.task_comment;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TaskCommentRequestDto(
        @NotNull(message = "Task ID cannot be null")
        Long taskId,
        @NotBlank(message = "Comment content must not be blank")
        String content
) { }
