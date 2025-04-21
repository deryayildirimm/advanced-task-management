package com.example.advancedtaskmanagement.task;

import com.example.advancedtaskmanagement.project.Project;
import com.example.advancedtaskmanagement.task.task_attachment.TaskAttachment;
import com.example.advancedtaskmanagement.user.User;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TaskDtoConverter {

    public TaskResponseDto toDto(Task task) {
        List<String> attachmentNames = task.getAttachments() != null
                ? task.getAttachments().stream()
                .map(TaskAttachment::getFileName)
                .toList()
                : List.of();

        return new TaskResponseDto(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getAcceptanceCriteria(),
                task.getPriority(),
                task.getStatus(),
                task.getAssignedUser() != null ? task.getAssignedUser().getName() : null,
                task.getProject() != null ? task.getProject().getTitle() : null,
                attachmentNames
        );
    }

    public Task toEntity(TaskRequestDto dto, Project project, User user) {
        return Task.builder()
                .title(dto.title())
                .description(dto.description())
                .acceptanceCriteria(dto.acceptanceCriteria())
                .priority(dto.priority())
                .status(dto.status())
                .project(project)
                .assignedUser(user)
                .build();
    }

    public void updateEntityFromDto(TaskRequestDto dto, Task task) {
        task.setTitle(dto.title());
        task.setDescription(dto.description());
        task.setAcceptanceCriteria(dto.acceptanceCriteria());
        task.setPriority(dto.priority());
        // assignedUser ayrı handle ediliyor zaten
    }
}
