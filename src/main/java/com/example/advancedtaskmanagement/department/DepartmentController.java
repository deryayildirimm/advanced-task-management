package com.example.advancedtaskmanagement.department;

import com.example.advancedtaskmanagement.project.ProjectResponseDto;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/departments")
public class DepartmentController {

    private final DepartmentService departmentService;

    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }


    @PostMapping
    public ResponseEntity<DepartmentResponseDto> createDepartment(@RequestBody @Valid DepartmentRequestDto departmentRequestDTO) {
        DepartmentResponseDto department = departmentService.createDepartment(departmentRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(department);
    }


    @PutMapping("/{departmentId}")
    public ResponseEntity<DepartmentResponseDto> updateDepartment(@PathVariable Long departmentId,
                                                                  @RequestBody @Valid DepartmentRequestDto departmentRequestDTO) {
        DepartmentResponseDto updatedDepartment = departmentService.updateDepartment(departmentId, departmentRequestDTO);
        return ResponseEntity.status(HttpStatus.OK).body(updatedDepartment);
    }

    @GetMapping
    public ResponseEntity<List<DepartmentResponseDto>> getAllDepartments() {
        List<DepartmentResponseDto> departments = departmentService.getAllDepartments();
        return ResponseEntity.ok(departments);
    }

    @GetMapping("/{departmentId}")
    public ResponseEntity<DepartmentResponseDto> getDepartmentById(@PathVariable Long departmentId) {
        DepartmentResponseDto department = departmentService.getDepartmentById(departmentId);
        return ResponseEntity.status(HttpStatus.OK).body(department);
    }


    @GetMapping("/{departmentId}/projects")
    public ResponseEntity<List<ProjectResponseDto>> getProjectsByDepartment(@PathVariable Long departmentId) {
        List<ProjectResponseDto> projects = departmentService.getProjectsByDepartment(departmentId);
        return ResponseEntity.status(HttpStatus.OK).body(projects);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDepartment(@PathVariable Long id) {
        departmentService.deleteDepartment(id);
        return ResponseEntity.noContent().build();
    }

}
