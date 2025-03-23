package com.example.advancedtaskmanagement.task;


import com.example.advancedtaskmanagement.project.Project;
import com.example.advancedtaskmanagement.project.ProjectRepository;
import com.example.advancedtaskmanagement.task.task_progress.TaskProgressService;
import com.example.advancedtaskmanagement.user.User;
import com.example.advancedtaskmanagement.user.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;


@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final TaskProgressService taskProgressService;
    private final TaskMapper taskMapper;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;



    public TaskService(
            TaskRepository taskRepository,
            TaskProgressService taskProgressService,
            TaskMapper taskMapper, ProjectRepository projectRepository, UserRepository userRepository
    ) {
        this.taskRepository = taskRepository;
        this.taskProgressService = taskProgressService;
        this.taskMapper = taskMapper;
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
    }

    public TaskResponseDto createTask(TaskRequestDto dto) {

        Task task = taskMapper.toEntity(dto);

        Project project = projectRepository.findById(dto.getProjectId())
                .orElseThrow(() -> new EntityNotFoundException("Project not found"));

        User assignee = userRepository.findById(dto.getAssignedUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        task.setProject(project);
        task.setAssignedUser(assignee);
        task.setPriority(dto.getPriority());
        task.setStatus(dto.getStatus());


        return taskMapper.toDto(taskRepository.save(task));
    }

    public List<TaskResponseDto> getAll() {
        return taskRepository.findByIsDeletedFalse().stream()
                .map(taskMapper::toDto)
                .toList();
    }

    public TaskResponseDto getById(Long id) {
        Task task = taskRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new EntityNotFoundException("Task not found"));
        return taskMapper.toDto(task);
    }

    public List<TaskResponseDto> getByProjectId(Long projectId) {
        return taskRepository.findByProjectIdAndIsDeletedFalse(projectId)
                .stream().map(taskMapper::toDto).toList();
    }

    public List<TaskResponseDto> getByAssigneeId(Long assigneeId) {
        return taskRepository.findByAssignedUserIdAndIsDeletedFalse(assigneeId)
                .stream().map(taskMapper::toDto).toList();
    }

    // Task status güncelleme
    public TaskResponseDto updateTaskStatus(Long taskId, TaskStatus status, String reason) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new EntityNotFoundException("Task not found"));

        task.setStatus(status);

        if (task.getStatus() == TaskStatus.COMPLETED) {
            throw new IllegalArgumentException("Completed tasks cannot be moved to any other state");
        }

        if ((status == TaskStatus.CANCELLED || status == TaskStatus.BLOCKED) &&
                (task.getStatus() != TaskStatus.IN_ANALYSIS && task.getStatus() != TaskStatus.IN_PROGRESS)) {
            throw new IllegalArgumentException("Invalid transition to Cancelled or Blocked");
        }

        if ((status == TaskStatus.CANCELLED || status == TaskStatus.BLOCKED) && (reason == null || reason.isEmpty())) {
            throw new IllegalArgumentException("Reason must be provided for Cancelled or Blocked status");
        }

        task.setStatus(status);

        Task updatedTask = taskRepository.save(task);

        taskProgressService.addTaskProgress(task, status, reason);

        return taskMapper.toTaskResponseDto(updatedTask);
    }

    public void delete(Long taskId) {

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new EntityNotFoundException("Task not found"));

        task.setDeleted(true);
        task.setDeletedAt(new Date());

        Long userId = getCurrentUserId();

        task.setDeletedBy(userId);

        taskRepository.save(task);
    }

    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof User user) {
            return user.getId();
        }
        throw new RuntimeException("User not authenticated");
    }


}
