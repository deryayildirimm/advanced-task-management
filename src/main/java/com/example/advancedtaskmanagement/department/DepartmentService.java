package com.example.advancedtaskmanagement.department;

import com.example.advancedtaskmanagement.project.Project;
import com.example.advancedtaskmanagement.project.ProjectMapper;
import com.example.advancedtaskmanagement.project.ProjectResponseDto;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final DepartmentMapper departmentMapper;
    private final ProjectMapper projectMapper;

    public DepartmentService(DepartmentRepository departmentRepository, DepartmentMapper departmentMapper, ProjectMapper projectMapper) {
        this.departmentRepository = departmentRepository;
        this.departmentMapper = departmentMapper;
        this.projectMapper = projectMapper;
    }

    public Department findDepartmentById(Long id) {
        return departmentRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    public DepartmentResponseDto createDepartment(DepartmentRequestDto departmentRequestDTO) {
        Department department = departmentMapper.departmentRequestDTOToDepartment(departmentRequestDTO);
        Department savedDepartment =  departmentRepository.save(department);

        return departmentMapper.departmentToDepartmentResponseDTO(savedDepartment);

    }


    public DepartmentResponseDto updateDepartment(Long departmentId, DepartmentRequestDto departmentRequestDTO) {

        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new EntityNotFoundException("Department not found"));

        department.setName(departmentRequestDTO.name());


        Department updatedDepartment = departmentRepository.save(department);

        return departmentMapper.departmentToDepartmentResponseDTO(updatedDepartment);
    }

    public DepartmentResponseDto getDepartmentById(Long departmentId) {

        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new EntityNotFoundException("Department not found"));

        return departmentMapper.departmentToDepartmentResponseDTO(department);
    }

    public List<ProjectResponseDto> getProjectsByDepartment(Long departmentId) {
        List<Project> projects = departmentRepository.findProjectsByDepartmentId(departmentId);
        return projects.stream()
                .map(projectMapper::projectToProjectResponseDTO)
                .collect(Collectors.toList());
    }






}
