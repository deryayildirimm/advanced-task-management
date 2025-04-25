package com.example.advancedtaskmanagement.task.task_progress;

import org.springframework.stereotype.Component;

@Component
public class TaskProgressMapper {

    public TaskProgressResponseDto toDto(TaskProgress taskProgress) {
        return new TaskProgressResponseDto(
                taskProgress.getId(),
                taskProgress.getStatus(),
                taskProgress.getReason(),
                taskProgress.getChangedAt()
        );
    }
}
