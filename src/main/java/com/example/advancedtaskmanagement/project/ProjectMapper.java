package com.example.advancedtaskmanagement.project;

import com.example.advancedtaskmanagement.department.Department;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface ProjectMapper {

    @Mapping(source = "departmentId", target = "department", qualifiedByName = "mapDepartmentFromId")
    Project toEntity(ProjectRequestDto dto);

    @Mapping(source = "department.name", target = "departmentName") // Entity -> DTO
    @Mapping(source = "id", target = "id")
    ProjectResponseDto toResponseDto(Project project);


    ProjectResponseDto projectToProjectResponseDTO(Project project);

    // Bu metot manuel olarak bir servisten alınan Department'i bağlayacak şekilde tasarlanmıştır.
    @Named("mapDepartmentFromId")
    default Department mapDepartmentFromId(Long departmentId) {
        if (departmentId == null) return null;
        Department dept = new Department();
        dept.setId(departmentId);
        return dept;
    }
}
