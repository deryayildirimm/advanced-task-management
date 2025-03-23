package com.example.advancedtaskmanagement.project;

import com.example.advancedtaskmanagement.task.TaskResponseDto;
import com.example.advancedtaskmanagement.task.TaskStatus;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("v1/projects")
public class ProjectController {

    /*
    create project
    update project
    update project status
    get project
    get all project
    get project tasks
    get projects by department
    delete project
    filter the projects

     */

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @PostMapping
    public ResponseEntity<ProjectResponseDto> createProject(@RequestBody ProjectRequestDto dto) {
        return new ResponseEntity<>(projectService.createProject(dto), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ProjectResponseDto>> getProjects() {
        return ResponseEntity.ok(projectService.getAllProjects());
    }

    @PutMapping("/{projectId}")
    public ResponseEntity<ProjectResponseDto> updateProject(@PathVariable Long projectId,
                                                            @RequestBody ProjectRequestDto projectRequestDto) {
        ProjectResponseDto updatedProject = projectService.updateProject(projectId, projectRequestDto);
        return ResponseEntity.status(HttpStatus.OK).body(updatedProject);
    }

    @PutMapping("/{projectId}/status")
    public ResponseEntity<ProjectResponseDto> updateProjectStatus(@PathVariable Long projectId,
                                                                  @RequestParam ProjectStatus status) {
        ProjectResponseDto updatedProject = projectService.updateProjectStatus(projectId, status);
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


    // 11. Update Task Status within a Project
    @PutMapping("/{projectId}/tasks/{taskId}/status")
    public ResponseEntity<TaskResponseDto> updateTaskStatus(@PathVariable Long projectId,
                                                            @PathVariable Long taskId,
                                                            @RequestParam TaskStatus status,
                                                            @RequestParam String reason) {
        TaskResponseDto updatedTask = projectService.updateTaskStatus(projectId, taskId, status, reason);
        return ResponseEntity.status(HttpStatus.OK).body(updatedTask);
    }

}
