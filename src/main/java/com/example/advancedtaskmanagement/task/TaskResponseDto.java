package com.example.advancedtaskmanagement.task;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class TaskResponseDto {

    private final Long id;
    private final String title;
    private final String description;
    private final String acceptanceCriteria;
    private final TaskPriority priority;
    private final TaskStatus status;
    private final String assignedUserName; // Kullanıcı adını dönelim
    private final String projectTitle; // Proje adını dönelim
    private final List<String> attachments;
 //   private final List<TaskCommentResponseDTO> comments;

}
