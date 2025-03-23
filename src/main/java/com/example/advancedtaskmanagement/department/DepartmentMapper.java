package com.example.advancedtaskmanagement.department;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DepartmentMapper {

    // DepartmentRequestDto -> Department dönüşümü
    Department departmentRequestDTOToDepartment(DepartmentRequestDto departmentRequestDTO);

    // Department -> DepartmentResponseDto dönüşümü
    DepartmentResponseDto departmentToDepartmentResponseDTO(Department department);

    // DepartmentResponseDto -> Department dönüşümü (Eğer gerekiyorsa)
    Department departmentResponseDTOToDepartment(DepartmentResponseDto departmentResponseDTO);

}
