package com.example.advancedtaskmanagement.task.task_attachment;

import org.springframework.stereotype.Component;

@Component
public class TaskAttachmentMapper {

    public TaskAttachmentResponseDto toDto(TaskAttachment taskAttachment) {

        return new TaskAttachmentResponseDto(
                taskAttachment.getId(),
                taskAttachment.getFilePath(),
                taskAttachment.getFileName(),
                taskAttachment.getUploadedAt()
        );

    }

}
