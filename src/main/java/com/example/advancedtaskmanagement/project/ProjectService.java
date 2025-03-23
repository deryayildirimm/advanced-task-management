package com.example.advancedtaskmanagement.project;

import com.example.advancedtaskmanagement.department.Department;
import com.example.advancedtaskmanagement.department.DepartmentRepository;
import com.example.advancedtaskmanagement.task.*;
import com.example.advancedtaskmanagement.user.User;
import com.example.advancedtaskmanagement.user.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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

    public ProjectService(ProjectRepository projectRepository,
                          ProjectMapper projectMapper,
                          TaskMapper taskMapper,
                          UserService userService,
                          TaskService taskService, DepartmentRepository departmentRepository) {
        this.projectRepository = projectRepository;
        this.projectMapper = projectMapper;
        this.taskMapper = taskMapper;
        this.userService = userService;
        this.taskService = taskService;
        this.departmentRepository = departmentRepository;
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

    // TODO : burada baya yapılacak ıslem var daha
    public ProjectResponseDto updateProject(Long id, ProjectRequestDto projectRequestDTO) {
        // Var olan projeyi bul
        Project existingProject = projectRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Project not found"));

        // Güncelleme işlemi
        existingProject.setTitle(projectRequestDTO.getTitle());
        existingProject.setDescription(projectRequestDTO.getDescription());
        existingProject.setStatus(projectRequestDTO.getStatus());
        existingProject.setStartDate(projectRequestDTO.getStartDate());
        existingProject.setEndDate(projectRequestDTO.getEndDate());


        // Güncellenmiş projeyi kaydet
        Project updatedProject = projectRepository.save(existingProject);

        // Güncellenen projeyi ProjectResponseDTO'ya çevir
        return projectMapper.toResponseDto(updatedProject);
    }
    public List<TaskResponseDto> getTasksByProject(Long projectId) {
        // Projeyi bul
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new EntityNotFoundException("Project not found"));

        // Projeye ait tüm task'ları al
        return project.getTasks().stream()
                .map(taskMapper::toTaskResponseDto) // taskMapper ile Task'ları DTO'ya dönüştür
                .collect(Collectors.toList());
    }

    public ProjectResponseDto updateProjectStatus(Long projectId, ProjectStatus status) {
        // Projeyi bul
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new EntityNotFoundException("Project not found"));

        // Durum güncelleme
        project.setStatus(status);

        // Projeyi güncelle
        Project updatedProject = projectRepository.save(project);

        // Response DTO'ya dönüştür
        return projectMapper.toResponseDto(updatedProject);
    }

    public TaskResponseDto updateTaskStatus(Long projectId, Long taskId, TaskStatus status, String reason) {

        // Projeyi bul
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new EntityNotFoundException("Project not found"));

        // Task'ı bul
    //    Task task = taskRepository.findById(taskId)
   //             .orElseThrow(() -> new EntityNotFoundException("Task not found"));

        // Task'ın bu projeye ait olup olmadığını kontrol et
      //  if (!task.getProject().getId().equals(project.getId())) {
      //      throw new IllegalArgumentException("Task does not belong to the specified project");
     //   }

        return taskService.updateTaskStatus(taskId, status, reason);
    }
/*
    public ProjectResponseDto assignMemberToProject(Long projectId, Long userId) {
        // Proje ve kullanıcıyı bul
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new EntityNotFoundException("Project not found"));

        User user = userService.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        // Kullanıcıyı projeye atama
        project.getTeamMembers().add(user);  // assuming there's a 'teamMembers' list in Project

        // Projeyi güncelle
        Project updatedProject = projectRepository.save(project);

        // ResponseDto'ya dönüştür
        return projectMapper.toResponseDto(updatedProject);
    }
*/

    public List<ProjectResponseDto> getProjectsByTitle(String title) {
        Optional<Project> projects = projectRepository.findByTitle(title);
        return projects.stream()
                .map(projectMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    public List<ProjectResponseDto> filterProjects(String title, String status, Long departmentId,  Date startDate, Date endDate) {


       List<Project> projects =  projectRepository.filterProjects(title, status, departmentId, startDate, endDate);

       return projects.stream().map(projectMapper::toResponseDto).collect(Collectors.toList());
    }

    public void deleteProject(Long projectId) {
        // Projeyi bul
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new EntityNotFoundException("Project not found"));

       project.setDeleted(true);
       project.setDeletedAt(new Date());
        // Kullanıcı ID'sini SecurityContext'ten alıyoruz
        Long userId = getCurrentUserId();

        // deletedBy alanına kullanıcı ID'sini setle
        project.setDeletedBy(userId);  // Kullanıcı ID'si (long türünde olmalı)

        // Güncellenmiş projeyi kaydet
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
