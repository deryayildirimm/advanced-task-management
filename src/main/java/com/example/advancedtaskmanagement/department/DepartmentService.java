package com.example.advancedtaskmanagement.department;

import com.example.advancedtaskmanagement.common.ErrorMessages;
import com.example.advancedtaskmanagement.exception.ExceptionHandler;
import com.example.advancedtaskmanagement.exception.ResourceNotFoundException;
import com.example.advancedtaskmanagement.project.Project;
import com.example.advancedtaskmanagement.project.ProjectMapper;
import com.example.advancedtaskmanagement.project.ProjectResponseDto;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final DepartmentMapper departmentMapper;
    private final ProjectMapper projectMapper;

    public DepartmentService(DepartmentRepository departmentRepository,
                             DepartmentMapper departmentMapper,
                             ProjectMapper projectMapper) {
        this.departmentRepository = departmentRepository;
        this.departmentMapper = departmentMapper;
        this.projectMapper = projectMapper;
    }

    public Department findDepartmentById(Long id) {
        return departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.DEPARTMENT_NOT_FOUND));
    }

    public DepartmentResponseDto createDepartment(DepartmentRequestDto departmentRequestDTO) {

        String name = departmentRequestDTO.name();

        ExceptionHandler.throwIf(name == null || name.trim().isEmpty() ,
                () -> new IllegalArgumentException(ErrorMessages.DEPARTMENT_NAME_BLANK));
        ExceptionHandler.throwIf(name.isEmpty() || name.length() > 50 ,
                () -> new IllegalArgumentException(ErrorMessages.DEPARTMENT_NAME_SIZE));
        ExceptionHandler.throwIf(departmentRepository.findByNameIgnoreCase(name.trim()).isPresent() ,
                () -> new IllegalArgumentException(ErrorMessages.DEPARTMENT_ALREADY_EXISTS));

        Department department = Department.builder()
                .name(name.trim())
                .projects(new ArrayList<>())
                .userList(new ArrayList<>())
                .build();

        Department savedDepartment =  departmentRepository.save(department);

        return departmentMapper.toDepartmentResponseDto(savedDepartment);

    }

    public List<DepartmentResponseDto> getAllDepartments() {
        return departmentRepository.findByIsDeletedFalse().stream()
                .map(departmentMapper::toDepartmentResponseDto)
                .toList();
    }

    public void deleteDepartment(Long departmentId) {
        Department department = findDepartmentById(departmentId);
        departmentRepository.delete(department);
    }


    public DepartmentResponseDto updateDepartment(Long departmentId, DepartmentRequestDto departmentRequestDTO) {

        Department department = findDepartmentById(departmentId);

        String name = departmentRequestDTO.name();

        ExceptionHandler.throwIf(name == null || name.trim().isEmpty() ,
                () -> new IllegalArgumentException(ErrorMessages.DEPARTMENT_NAME_BLANK));
        ExceptionHandler.throwIf(name.isEmpty() || name.length() > 50 ,
                () -> new IllegalArgumentException(ErrorMessages.DEPARTMENT_NAME_SIZE));
        ExceptionHandler.throwIf(departmentRepository.findByNameIgnoreCase(name.trim()).isPresent() ,
                () -> new IllegalArgumentException(ErrorMessages.DEPARTMENT_ALREADY_EXISTS));

        department.setName(name.trim());

        Department updatedDepartment = departmentRepository.save(department);

        return departmentMapper.toDepartmentResponseDto(updatedDepartment);
    }

    public DepartmentResponseDto getDepartmentById(Long departmentId) {

        Department department = findDepartmentById(departmentId);

        return departmentMapper.toDepartmentResponseDto(department);
    }

    public List<ProjectResponseDto> getProjectsByDepartment(Long departmentId) {
        List<Project> projects = departmentRepository.findProjectsByDepartmentId(departmentId);
        return projects.stream()
                .map(projectMapper::toResponseDto)
                .toList();
    }






}
