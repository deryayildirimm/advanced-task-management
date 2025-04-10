package com.example.advancedtaskmanagement.task;


import com.example.advancedtaskmanagement.common.ErrorMessages;
import com.example.advancedtaskmanagement.exception.ResourceNotFoundException;
import com.example.advancedtaskmanagement.project.Project;
import com.example.advancedtaskmanagement.project.ProjectRepository;
import com.example.advancedtaskmanagement.project.project_user_assignment.ProjectUserAssignmentRepository;
import com.example.advancedtaskmanagement.task.task_progress.TaskProgressService;
import com.example.advancedtaskmanagement.user.User;
import com.example.advancedtaskmanagement.user.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;


@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final TaskProgressService taskProgressService;
    private final TaskMapper taskMapper;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final ProjectUserAssignmentRepository projectUserAssignmentRepository;



    public TaskService(
            TaskRepository taskRepository,
            TaskProgressService taskProgressService,
            TaskMapper taskMapper,
            ProjectRepository projectRepository,
            UserRepository userRepository,
            ProjectUserAssignmentRepository projectUserAssignmentRepository
    ) {
        this.taskRepository = taskRepository;
        this.taskProgressService = taskProgressService;
        this.taskMapper = taskMapper;
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
        this.projectUserAssignmentRepository = projectUserAssignmentRepository;
    }

    /*  genel kullanılan metodlar */
    protected Task getTaskById(Long id) {
        return taskRepository.findByIdAndIsDeletedFalse(id).orElseThrow( () -> new ResourceNotFoundException(ErrorMessages.TASK_NOT_FOUND));
    }
    public TaskResponseDto getById(Long id) {
        Task task = getTaskById(id);
        return taskMapper.toDto(task);
    }

    public Task generateTask(Project project , TaskRequestDto taskDto){

        User newUser = userRepository.findById(taskDto.assignedUserId())
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.USER_NOT_FOUND));

        Task newTask = Task.builder()
                .priority(taskDto.priority())
                .description(taskDto.description())
                .project(project)
                .acceptanceCriteria(taskDto.acceptanceCriteria())
                .status(taskDto.status())
                .title(taskDto.title())
                .assignedUser(newUser)
                .build();

        return taskRepository.save(newTask);
    }


    // burada güncellemeler olacak
    /*
    * bu taska atanan kullanıcı projede var mı ?  -> check projectuserassignee table
    *
     */
    public TaskResponseDto createTask( Long projectId, TaskRequestDto dto) {

        Task task = taskMapper.toEntity(dto);

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.PROJECT_NOT_FOUND));

        User assignee = userRepository.findById(dto.assignedUserId())
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.USER_NOT_FOUND));

        task.setProject(project);
        task.setAssignedUser(assignee);
        task.setPriority(dto.priority());
        task.setStatus(dto.status());


        return taskMapper.toDto(taskRepository.save(task));
    }

    public List<TaskResponseDto> getAll() {
        return taskRepository.findByIsDeletedFalse().stream()
                .map(taskMapper::toDto)
                .toList();
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
    public TaskResponseDto updateTaskStatus(Long taskId, TaskStatusRequest request) {
        Task task = getTaskById(taskId);

        task.setStatus(request.status());

        if (task.getStatus() == TaskStatus.COMPLETED) {
            throw new IllegalArgumentException("Completed tasks cannot be moved to any other state");
        }

        if ((request.status() == TaskStatus.CANCELLED || request.status() == TaskStatus.BLOCKED) &&
                (task.getStatus() != TaskStatus.IN_ANALYSIS && task.getStatus() != TaskStatus.IN_PROGRESS)) {
            throw new IllegalArgumentException("Invalid transition to Cancelled or Blocked");
        }

        if ((request.status() == TaskStatus.CANCELLED || request.status() == TaskStatus.BLOCKED) && (request.reason() == null || request.reason().isEmpty())) {
            throw new IllegalArgumentException("Reason must be provided for Cancelled or Blocked status");
        }

        task.setStatus(request.status());

        Task updatedTask = taskRepository.save(task);

        taskProgressService.addTaskProgress(task, request.status(), request.reason());

        return taskMapper.toTaskResponseDto(updatedTask);
    }

    public void delete(Long taskId) {

        Task task = getTaskById(taskId);

        task.setDeleted(true);
        task.setDeletedAt(LocalDateTime.now());

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
