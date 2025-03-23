package com.example.advancedtaskmanagement.task;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface TaskMapper {


    TaskResponseDto toDto(Task task);


    Task toEntity(TaskRequestDto dto);


    void updateEntityFromDto(TaskRequestDto dto, @MappingTarget Task entity);

    TaskResponseDto toTaskResponseDto(Task task);
}
