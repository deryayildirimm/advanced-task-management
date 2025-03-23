package com.example.advancedtaskmanagement.task.task_progress;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface TaskProgressMapper {

    TaskProgressMapper INSTANCE = Mappers.getMapper(TaskProgressMapper.class);

    TaskProgressResponseDto toResponseDto(TaskProgress taskProgress);
}
