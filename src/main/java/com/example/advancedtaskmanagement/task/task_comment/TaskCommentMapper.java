package com.example.advancedtaskmanagement.task.task_comment;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface TaskCommentMapper {


    TaskCommentResponseDto toDto(TaskComment taskComment);

    TaskComment toEntity(TaskCommentRequestDto dto);

    void updateEntityFromDto(TaskCommentRequestDto dto, @MappingTarget TaskComment entity);
}
