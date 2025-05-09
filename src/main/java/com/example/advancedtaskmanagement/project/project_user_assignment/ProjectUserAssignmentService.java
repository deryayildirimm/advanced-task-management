package com.example.advancedtaskmanagement.project.project_user_assignment;

import com.example.advancedtaskmanagement.common.ErrorMessages;
import com.example.advancedtaskmanagement.exception.ResourceNotFoundException;
import com.example.advancedtaskmanagement.project.Project;
import com.example.advancedtaskmanagement.project.ProjectRepository;
import com.example.advancedtaskmanagement.security.AuthService;
import com.example.advancedtaskmanagement.user.User;
import com.example.advancedtaskmanagement.user.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Service
public class ProjectUserAssignmentService {

    private final ProjectUserAssignmentRepository assignmentRepository;
    private final ProjectUserAssignmentMapper mapper;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final AuthService authService;

    public ProjectUserAssignmentService(ProjectUserAssignmentRepository assignmentRepository,
                                        ProjectUserAssignmentMapper mapper,
                                        ProjectRepository projectRepository,
                                        UserRepository userRepository,
                                        AuthService authService) {
        this.assignmentRepository = assignmentRepository;
        this.mapper = mapper;
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
        this.authService = authService;
    }

    public void softDelete(Long id) {
        var assignment = assignmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Assignment not found"));

        assignment.setDeleted(true);
        assignment.setDeletedAt(LocalDateTime.now());
        assignment.setDeletedBy(authService.getCurrentUser().getId());
        assignmentRepository.save(assignment);
    }

    public List<ProjectUserAssignmentResponseDto> getAllAssignments() {
        return assignmentRepository.findAllByIsDeletedFalse()
                .stream()
                .filter(a -> !a.isDeleted())
                .map(mapper::toDto)
                .toList();
    }

    public List<ProjectUserAssignmentResponseDto> getAssignmentsByProjectId(Long projectId) {
        List<ProjectUserAssignment> assignments = assignmentRepository.findByProjectIdAndIsDeletedFalse(projectId);
        return assignments.stream()
                .map(mapper::toDto)
                .toList();
    }

    public ProjectUserAssignmentResponseDto assignUserToProject(ProjectUserAssignmentRequestDto request) {

        Project project = projectRepository.findById(request.projectId())
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.PROJECT_NOT_FOUND));

        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.USER_NOT_FOUND));


        ProjectUserAssignment assignment = new ProjectUserAssignment();
        assignment.setProject(project);
        assignment.setUser(user);
        assignment.setRole(request.role());

        ProjectUserAssignment saved = assignmentRepository.save(assignment);

        return mapper.toDto(saved);
    }

    public List<TeamMemberDto> getTeamMembersByProjectId(Long projectId) {
        List<ProjectUserAssignment> assignments = assignmentRepository.findByProjectIdWithUser(projectId);

        return assignments.stream()
                .map(a -> {
                    User u = a.getUser();
                    return new TeamMemberDto(
                            u.getId(),
                            u.getUsername(),
                            a.getRole().name()
                    );
                })
                .toList();
    }
}
