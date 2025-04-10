package com.example.advancedtaskmanagement.task;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;


public record TaskResponseDto (
        Long id,
        String title,
        String description,
        String acceptanceCriteria,
        TaskPriority priority,
        TaskStatus status,
        String assignedUserName,
        String projectTitle,
        List<String> attachments
){ }
