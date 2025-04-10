package com.example.advancedtaskmanagement.project;

import com.example.advancedtaskmanagement.common.BaseService;
import com.example.advancedtaskmanagement.common.ErrorMessages;
import com.example.advancedtaskmanagement.department.Department;
import com.example.advancedtaskmanagement.department.DepartmentService;
import com.example.advancedtaskmanagement.exception.ExceptionHandler;
import com.example.advancedtaskmanagement.exception.ResourceNotFoundException;
import com.example.advancedtaskmanagement.exception.UserAlreadyAssignedException;
import com.example.advancedtaskmanagement.project.project_user_assignment.ProjectUserAssignment;
import com.example.advancedtaskmanagement.project.project_user_assignment.ProjectUserAssignmentRepository;
import com.example.advancedtaskmanagement.project.project_user_assignment.ProjectUserAssignmentRequestDto;
import com.example.advancedtaskmanagement.task.*;
import com.example.advancedtaskmanagement.user.Role;
import com.example.advancedtaskmanagement.user.User;
import com.example.advancedtaskmanagement.user.UserService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProjectService extends BaseService<Project> {

    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;
    private final TaskMapper taskMapper;
    private final DepartmentService departmentService;
    private final ProjectUserAssignmentRepository projectUserAssignmentRepository;
    private final UserService userService;

    public ProjectService(ProjectRepository projectRepository,
                          ProjectMapper projectMapper,
                          TaskMapper taskMapper,
                          DepartmentService departmentService,
                          ProjectUserAssignmentRepository projectUserAssignmentRepository,
                          UserService userService) {
        super(projectRepository);
        this.projectRepository = projectRepository;
        this.projectMapper = projectMapper;
        this.taskMapper = taskMapper;
        this.departmentService = departmentService;
        this.projectUserAssignmentRepository = projectUserAssignmentRepository;
        this.userService = userService;
    }

    public ProjectResponseDto createProject(ProjectRequestDto dto) {

        // is there a department ?
        Department department = departmentService.findDepartmentById(dto.departmentId());

        ExceptionHandler.throwIf(dto.status().equals(ProjectStatus.CANCELLED) || dto.status().equals(ProjectStatus.COMPLETED) ,
                () -> new IllegalArgumentException(ErrorMessages.PROJECT_STATUS_CANNOT_BE_CANCELLED_OR_COMPLETED_ON_CREATE));
       ExceptionHandler.throwIf(!dto.startDate().isBefore(dto.endDate()) ,
               () -> new IllegalArgumentException(ErrorMessages.START_DATE_BEFORE_END_DATE));


        // create a new project
        Project newProject = Project.builder().
                title(dto.title()).
                description(dto.description()).
                status(dto.status()).
                startDate(dto.startDate()).
                endDate(dto.endDate()).
                department(department).
                build();

        save(newProject);

        // turn responseDTO and return
        return projectMapper.toResponseDto(newProject);
    }

    public List<ProjectResponseDto> getAllProjects() {
        return projectRepository.findByIsDeletedFalse().stream()
                .map(projectMapper::toResponseDto)
                .collect(Collectors.toList());
    }


    public ProjectResponseDto getProjectById(Long id) {

        Project project = findById(id);
        return projectMapper.toResponseDto(project);
    }


    public ProjectResponseDto updateProject(Long id, ProjectRequestDto projectRequestDTO) {

        Project existingProject = findById(id);

        Department updateDepartment = departmentService.findDepartmentById(projectRequestDTO.departmentId());

        ExceptionHandler.throwIf(!projectRequestDTO.startDate().isBefore(projectRequestDTO.endDate()) ,
                () ->   new IllegalArgumentException(ErrorMessages.START_DATE_BEFORE_END_DATE)  );

        existingProject.setTitle(projectRequestDTO.title());
        existingProject.setDescription(projectRequestDTO.description());
        existingProject.setStatus(projectRequestDTO.status());
        existingProject.setStartDate(projectRequestDTO.startDate());
        existingProject.setEndDate(projectRequestDTO.endDate());
        existingProject.setDepartment(updateDepartment);

        Project updatedProject = save(existingProject);

        return projectMapper.toResponseDto(updatedProject);
    }

    public List<TaskResponseDto> getTasksByProject(Long projectId) {

        Project project = findById(projectId);

        return project.getTasks().stream()
                .map(taskMapper::toTaskResponseDto)
                .collect(Collectors.toList());
    }

    private void checkIfUserAlreadyAssign(Long projectId, Long assignedUserId){
        boolean isAssigned = projectUserAssignmentRepository
                .findByProjectIdAndUserId(projectId, assignedUserId).isPresent();
        ExceptionHandler.throwIf(isAssigned, () -> new UserAlreadyAssignedException(ErrorMessages.USER_ALREADY_ASSIGNED));

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

      softDelete(projectId);
    }

    public ProjectUserAssignment assignUserToProject(Long projectId, ProjectUserAssignmentRequestDto requestDto) {

        Project project =findById(projectId);
        User user = userService.findByUserId(requestDto.userId());

        checkIfUserAlreadyAssign(project.getId(), user.getId());


        ProjectUserAssignment projectUserAssignment = ProjectUserAssignment.builder()
                .project(project)
                .user(user)
                .role(requestDto.role())
                .assignedAt(LocalDate.now())
                .build();

      return  projectUserAssignmentRepository.save(projectUserAssignment);

    }
    public ProjectUserAssignment updateUserToProject(Long projectId, Long userId, Role role) {

        ProjectUserAssignment projectUserAssignment = projectUserAssignmentRepository.findByProjectIdAndUserId(projectId, userId)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.USER_NOT_ASSIGNED));

        projectUserAssignment.setRole(role);

        return projectUserAssignmentRepository.save(projectUserAssignment);
    }

    public void deleteUserFromProject(Long projectId, Long userId) {

        ProjectUserAssignment projectUserAssignment = projectUserAssignmentRepository.findByProjectIdAndUserId(projectId, userId)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.USER_NOT_ASSIGNED));

        projectUserAssignmentRepository.delete(projectUserAssignment);

    }

}
