package com.example.advancedtaskmanagement.task;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TaskMapper {


    TaskResponseDto toTaskResponseDto(Task task);
}
