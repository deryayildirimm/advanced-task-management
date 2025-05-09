package com.example.advancedtaskmanagement.project.project_user_assignment;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/project-user-assignments")
public class ProjectUserAssignmentController {

    private final ProjectUserAssignmentService assignmentService;

    public ProjectUserAssignmentController(ProjectUserAssignmentService assignmentService) {
        this.assignmentService = assignmentService;
    }


    @PostMapping
    public ResponseEntity<ProjectUserAssignmentResponseDto> assignUserToProject(@RequestBody @Valid ProjectUserAssignmentRequestDto request) {

        return ResponseEntity.ok(assignmentService.assignUserToProject(request));
    }

    @GetMapping("/project/{projectId}")
    public ResponseEntity<List<ProjectUserAssignmentResponseDto>> getUsersByProject(@PathVariable Long projectId) {
        return ResponseEntity.ok(assignmentService.getAssignmentsByProjectId(projectId));
    }

    @DeleteMapping("/{assignmentId}")
    public ResponseEntity<Void> removeUserFromProject(@PathVariable Long assignmentId) {
        assignmentService.softDelete(assignmentId);
        return ResponseEntity.noContent().build();

    }

    @GetMapping("/project/{projectId}/members")
    public ResponseEntity<List<TeamMemberDto>> getProjectMembers(@PathVariable Long projectId) {
        List<TeamMemberDto> members = assignmentService.getTeamMembersByProjectId(projectId);
        return ResponseEntity.ok(members);
    }
}
