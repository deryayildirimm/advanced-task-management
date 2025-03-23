package com.example.advancedtaskmanagement.task.task_progress;

import com.example.advancedtaskmanagement.task.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Date;

@Getter
@AllArgsConstructor
public class TaskProgressResponseDto {

    private Long id;
    private TaskStatus status;
    private String reason;
    private Date changedAt;

}
