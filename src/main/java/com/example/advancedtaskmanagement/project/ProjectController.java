package com.example.advancedtaskmanagement.project;

import com.example.advancedtaskmanagement.project.project_user_assignment.ProjectUserAssignment;
import com.example.advancedtaskmanagement.project.project_user_assignment.ProjectUserAssignmentRequestDto;
import com.example.advancedtaskmanagement.task.TaskResponseDto;
import com.example.advancedtaskmanagement.user.Role;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("v1/projects")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @PostMapping
    public ResponseEntity<ProjectResponseDto> createProject(@Valid @RequestBody ProjectRequestDto dto) {
        return new ResponseEntity<>(projectService.createProject(dto), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ProjectResponseDto>> getProjects() {
        return ResponseEntity.ok(projectService.getAllProjects());
    }

    @PutMapping("/{projectId}")
    public ResponseEntity<ProjectResponseDto> updateProject(@Valid  @PathVariable Long projectId,
                                                            @RequestBody ProjectRequestDto projectRequestDto) {
        ProjectResponseDto updatedProject = projectService.updateProject(projectId, projectRequestDto);
        return ResponseEntity.status(HttpStatus.OK).body(updatedProject);
    }


    @GetMapping("/{projectId}")
    public ResponseEntity<ProjectResponseDto> getProjectById(@PathVariable Long projectId) {
        ProjectResponseDto project = projectService.getProjectById(projectId);
        return ResponseEntity.status(HttpStatus.OK).body(project);
    }


    @GetMapping("/{projectId}/tasks")
    public ResponseEntity<List<TaskResponseDto>> getTasksByProject(@PathVariable Long projectId) {
        List<TaskResponseDto> tasks = projectService.getTasksByProject(projectId);
        return ResponseEntity.status(HttpStatus.OK).body(tasks);
    }


    @DeleteMapping("/{projectId}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long projectId) {
        projectService.deleteProject(projectId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping("{projectId}/assignees")
    public ResponseEntity<ProjectUserAssignment> assignUser(@PathVariable Long projectId, @RequestBody ProjectUserAssignmentRequestDto requestDto){
      ProjectUserAssignment projectUserAssignment =  projectService.assignUserToProject(projectId, requestDto);
      // 201 created
        return ResponseEntity.status(HttpStatus.CREATED).body(projectUserAssignment);
    }

    @PutMapping("{projectId}/assignees/{userId}")
    public ResponseEntity<ProjectUserAssignment> updateUserRole(
            @PathVariable Long projectId,
            @PathVariable Long userId,
            @RequestParam @Valid Role role
            ) {

        ProjectUserAssignment userAssignment = projectService.updateUserToProject(projectId, userId, role);

        return ResponseEntity.status(HttpStatus.CREATED).body(userAssignment);

    }

    @DeleteMapping("{projectId}/assignees/{userId}")
    public ResponseEntity<Void> deleteUser(
            @PathVariable Long projectId,
            @PathVariable Long userId) {
         projectService.deleteUserFromProject(projectId,userId);
         return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


    @GetMapping("/filter")
    public ResponseEntity<List<ProjectResponseDto>> filterProjects(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) ProjectStatus status,
            @RequestParam(required = false) Long departmentId,
            @RequestParam(required = false) Date startDate,
            @RequestParam(required = false) Date endDate) {

        List<ProjectResponseDto> projects = projectService.filterProjects(title, status, departmentId, startDate, endDate);

        if (projects.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        return ResponseEntity.status(HttpStatus.OK).body(projects);
    }



}
