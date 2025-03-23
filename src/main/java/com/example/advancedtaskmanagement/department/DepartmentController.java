package com.example.advancedtaskmanagement.department;

import com.example.advancedtaskmanagement.project.ProjectResponseDto;
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

    // 1. Departman oluşturma
    @PostMapping
    public ResponseEntity<DepartmentResponseDto> createDepartment(@RequestBody DepartmentRequestDto departmentRequestDTO) {
        DepartmentResponseDto department = departmentService.createDepartment(departmentRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(department);
    }

    // 2. Departman güncelleme
    @PutMapping("/{departmentId}")
    public ResponseEntity<DepartmentResponseDto> updateDepartment(@PathVariable Long departmentId,
                                                                  @RequestBody DepartmentRequestDto departmentRequestDTO) {
        DepartmentResponseDto updatedDepartment = departmentService.updateDepartment(departmentId, departmentRequestDTO);
        return ResponseEntity.status(HttpStatus.OK).body(updatedDepartment);
    }

    // 3. Departman bilgilerini getirme
    @GetMapping("/{departmentId}")
    public ResponseEntity<DepartmentResponseDto> getDepartmentById(@PathVariable Long departmentId) {
        DepartmentResponseDto department = departmentService.getDepartmentById(departmentId);
        return ResponseEntity.status(HttpStatus.OK).body(department);
    }

    // 4. Departmana ait projeleri getirme
    @GetMapping("/{departmentId}/projects")
    public ResponseEntity<List<ProjectResponseDto>> getProjectsByDepartment(@PathVariable Long departmentId) {
        List<ProjectResponseDto> projects = departmentService.getProjectsByDepartment(departmentId);
        return ResponseEntity.status(HttpStatus.OK).body(projects);
    }

}
