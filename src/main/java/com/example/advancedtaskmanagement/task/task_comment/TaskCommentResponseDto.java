package com.example.advancedtaskmanagement.task.task_comment;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Date;

@Getter
@AllArgsConstructor
public class TaskCommentResponseDto {

    private Long id;
    private String content;
    private Date createdAt;
    private Long userId;
}
