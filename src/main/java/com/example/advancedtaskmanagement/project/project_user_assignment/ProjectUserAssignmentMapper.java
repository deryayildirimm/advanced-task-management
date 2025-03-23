package com.example.advancedtaskmanagement.project.project_user_assignment;

import com.example.advancedtaskmanagement.project.Project;
import com.example.advancedtaskmanagement.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProjectUserAssignmentMapper {

    @Mapping(source = "project.id", target = "projectId")
    @Mapping(source = "user.id", target = "userId")
    ProjectUserAssignmentResponseDto toDto(ProjectUserAssignment assignment);


    @Mapping(target = "project", source = "project")
    @Mapping(target = "user", source = "user")
    ProjectUserAssignment toEntity(ProjectUserAssignmentRequestDto dto, Project project, User user);

    void updateAssignmentFromDto(ProjectUserAssignmentRequestDto dto, @MappingTarget ProjectUserAssignment assignment);
}
