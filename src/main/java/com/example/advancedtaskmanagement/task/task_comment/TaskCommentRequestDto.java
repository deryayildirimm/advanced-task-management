package com.example.advancedtaskmanagement.task.task_comment;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TaskCommentRequestDto {

    private Long taskId;
    private Long userId;
    private String comment;

}
