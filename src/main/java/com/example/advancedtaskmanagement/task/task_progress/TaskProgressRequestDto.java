package com.example.advancedtaskmanagement.task.task_progress;

import com.example.advancedtaskmanagement.task.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TaskProgressRequestDto {

    private Long taskId;
    private TaskStatus status;
    private String reason;
}
