package com.example.advancedtaskmanagement.task.task_progress;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface TaskProgressMapper {

    TaskProgressResponseDto toResponseDto(TaskProgress taskProgress);
}
