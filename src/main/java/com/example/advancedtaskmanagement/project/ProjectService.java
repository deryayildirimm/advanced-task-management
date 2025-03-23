package com.example.advancedtaskmanagement.project;

import com.example.advancedtaskmanagement.department.Department;
import com.example.advancedtaskmanagement.department.DepartmentRepository;
import com.example.advancedtaskmanagement.project.project_user_assignment.ProjectUserAssignmentService;
import com.example.advancedtaskmanagement.task.*;
import com.example.advancedtaskmanagement.user.User;
import com.example.advancedtaskmanagement.user.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;
    private final TaskMapper taskMapper;
    private final UserService userService;
    private final TaskService taskService;
    private final DepartmentRepository departmentRepository;
    private final ProjectUserAssignmentService projectUserAssignmentService;

    public ProjectService(ProjectRepository projectRepository,
                          ProjectMapper projectMapper,
                          TaskMapper taskMapper,
                          UserService userService,
                          TaskService taskService, DepartmentRepository departmentRepository, ProjectUserAssignmentService projectUserAssignmentService) {
        this.projectRepository = projectRepository;
        this.projectMapper = projectMapper;
        this.taskMapper = taskMapper;
        this.userService = userService;
        this.taskService = taskService;
        this.departmentRepository = departmentRepository;
        this.projectUserAssignmentService = projectUserAssignmentService;
    }

    public ProjectResponseDto createProject(ProjectRequestDto dto) {

        Department department = departmentRepository.findById(dto.getDepartmentId())
                .orElseThrow(() -> new EntityNotFoundException("Department not found"));
        Project project = projectMapper.toEntity(dto);
        project.setDepartment(department);
        Project savedProject = projectRepository.save(project);
        return projectMapper.toResponseDto(savedProject);
    }

    public List<ProjectResponseDto> getAllProjects() {
        return projectRepository.findByIsDeletedFalse().stream()
                .map(projectMapper::toResponseDto)
                .collect(Collectors.toList());
    }


    public ProjectResponseDto getProjectById(Long id) {

        Project project = projectRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Project not found"));
        return projectMapper.toResponseDto(project);
    }


    public ProjectResponseDto updateProject(Long id, ProjectRequestDto projectRequestDTO) {

        Project existingProject = projectRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Project not found"));


        existingProject.setTitle(projectRequestDTO.getTitle());
        existingProject.setDescription(projectRequestDTO.getDescription());
        existingProject.setStatus(projectRequestDTO.getStatus());
        existingProject.setStartDate(projectRequestDTO.getStartDate());
        existingProject.setEndDate(projectRequestDTO.getEndDate());



        Project updatedProject = projectRepository.save(existingProject);


        return projectMapper.toResponseDto(updatedProject);
    }
    public List<TaskResponseDto> getTasksByProject(Long projectId) {

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new EntityNotFoundException("Project not found"));


        return project.getTasks().stream()
                .map(taskMapper::toTaskResponseDto)
                .collect(Collectors.toList());
    }

    public ProjectResponseDto updateProjectStatus(Long projectId, ProjectStatus status) {

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new EntityNotFoundException("Project not found"));

        project.setStatus(status);
        Project updatedProject = projectRepository.save(project);


        return projectMapper.toResponseDto(updatedProject);
    }

    public TaskResponseDto updateTaskStatus(Long projectId, Long taskId, TaskStatus status, String reason) {

        // Projeyi bul
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new EntityNotFoundException("Project not found"));

        return taskService.updateTaskStatus(taskId, status, reason);
    }

    public List<ProjectResponseDto> getProjectsByTitle(String title) {
        Optional<Project> projects = projectRepository.findByTitle(title);
        return projects.stream()
                .map(projectMapper::toResponseDto)
                .collect(Collectors.toList());
    }

        public List<ProjectResponseDto> filterProjects(String title, ProjectStatus status, Long departmentId,  Date startDate, Date endDate) {


       List<Project> projects =  projectRepository.filterProjects(title, status, departmentId, startDate, endDate);

       return projects.stream().map(projectMapper::toResponseDto).collect(Collectors.toList());
    }

    public void deleteProject(Long projectId) {

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new EntityNotFoundException("Project not found"));

       project.setDeleted(true);
       project.setDeletedAt(new Date());

        Long userId = getCurrentUserId();


        project.setDeletedBy(userId);

        projectRepository.save(project);
    }

    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof User user) {
            return user.getId();
        }
        throw new RuntimeException("User not authenticated");
    }


}
