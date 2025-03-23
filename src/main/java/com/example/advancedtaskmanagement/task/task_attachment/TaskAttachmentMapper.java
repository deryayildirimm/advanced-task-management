package com.example.advancedtaskmanagement.task.task_attachment;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TaskAttachmentMapper {

    TaskAttachmentResponseDto toDto(TaskAttachment taskAttachment);

    TaskAttachment toEntity(TaskAttachmentResponseDto taskAttachmentResponseDto);

    TaskAttachmentRequestDto toTaskAttachmentRequestDto(TaskAttachmentResponseDto taskAttachment);

}
