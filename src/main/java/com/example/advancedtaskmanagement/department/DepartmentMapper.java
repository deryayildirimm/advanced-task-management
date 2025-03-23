package com.example.advancedtaskmanagement.department;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DepartmentMapper {


    Department departmentRequestDTOToDepartment(DepartmentRequestDto departmentRequestDTO);

    DepartmentResponseDto departmentToDepartmentResponseDTO(Department department);


}
