package com.example.advancedtaskmanagement.project;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ProjectMapper {

    ProjectMapper INSTANCE = Mappers.getMapper(ProjectMapper.class);

    Project toEntity(ProjectRequestDto dto);

    @Mapping(source = "id", target = "id")
    ProjectResponseDto toResponseDto(Project project);

}
