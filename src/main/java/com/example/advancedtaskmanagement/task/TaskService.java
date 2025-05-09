package com.example.advancedtaskmanagement.task;


import com.example.advancedtaskmanagement.common.BaseService;
import com.example.advancedtaskmanagement.common.ErrorMessages;
import com.example.advancedtaskmanagement.common.Messages;
import com.example.advancedtaskmanagement.exception.BusinessException;
import com.example.advancedtaskmanagement.exception.ExceptionHandler;
import com.example.advancedtaskmanagement.exception.ResourceNotFoundException;
import com.example.advancedtaskmanagement.project.Project;
import com.example.advancedtaskmanagement.project.ProjectService;
import com.example.advancedtaskmanagement.project.project_user_assignment.ProjectUserAssignmentRepository;
import com.example.advancedtaskmanagement.task.task_progress.TaskProgressService;
import com.example.advancedtaskmanagement.user.User;
import com.example.advancedtaskmanagement.user.UserRepository;

import org.springframework.stereotype.Service;


import java.util.List;


@Service
public class TaskService extends BaseService<Task> {

    private final TaskRepository taskRepository;
    private final TaskProgressService taskProgressService;
    private final UserRepository userRepository;
    private final ProjectUserAssignmentRepository projectUserAssignmentRepository;
    private final ProjectService projectService;
    private final TaskMapper taskMapper;


    public TaskService(
            TaskRepository taskRepository,
            TaskProgressService taskProgressService,
            UserRepository userRepository,
            ProjectUserAssignmentRepository projectUserAssignmentRepository,
            ProjectService projectService, TaskMapper taskMapper) {
        super(taskRepository);
        this.taskRepository = taskRepository;
        this.taskProgressService = taskProgressService;
        this.userRepository = userRepository;
        this.projectUserAssignmentRepository = projectUserAssignmentRepository;
        this.projectService = projectService;
        this.taskMapper = taskMapper;
    }


    public TaskResponseDto getById(Long id) {
        Task task = findById(id);
        return taskMapper.toDto(task);
    }

    public TaskResponseDto createTask( Long projectId, TaskRequestDto dto) {

        Project project = projectService.findById(projectId);
        User user = userRepository.findById(dto.assignedUserId())
                .orElseThrow( () -> new ResourceNotFoundException(ErrorMessages.USER_NOT_FOUND));


        boolean isAssigned = projectUserAssignmentRepository.findByProjectIdAndUserId(projectId, dto.assignedUserId())
                .isPresent();
        if(!isAssigned){
            throw new ResourceNotFoundException(ErrorMessages.USER_NOT_ASSIGNED);
        }

        Task task = Task.builder()
                .title(dto.title())
                .description(dto.description())
                .priority(dto.priority())
                .acceptanceCriteria(dto.acceptanceCriteria())
                .status(dto.status())
                .assignedUser(user)
                .project(project)
                .build();

      Task savedTask =   taskRepository.save(task);

        taskProgressService.logProgress(task,task.getStatus(), Messages.TASK_CREATED);

        return taskMapper.toDto(savedTask);

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


    public TaskResponseDto updateTaskStatus(Long taskId, TaskStatusRequest request) {
        Task task = findById(taskId);

        TaskStatus taskStatus = task.getStatus();
        TaskStatus updatedTaskStatus = request.status();

        ExceptionHandler.throwIf(updatedTaskStatus == null || updatedTaskStatus.equals(taskStatus) ,
                () -> new BusinessException(ErrorMessages.STATUS_SAME_AS_CURRENT));

        ExceptionHandler.throwIf(task.getStatus() == TaskStatus.COMPLETED ,
                () -> new BusinessException(ErrorMessages.STATUS_ALREADY_COMPLETED));

        ExceptionHandler.throwIf(
                (request.status() == TaskStatus.CANCELLED || request.status() == TaskStatus.BLOCKED) &&
                        (task.getStatus() != TaskStatus.IN_ANALYSIS && task.getStatus() != TaskStatus.IN_PROGRESS),
                () -> new BusinessException(ErrorMessages.STATUS_TRANSITION_INVALID)
        );

        ExceptionHandler.throwIf(
                (request.status() == TaskStatus.CANCELLED || request.status() == TaskStatus.BLOCKED)
                        && (request.reason() == null || request.reason().isEmpty()),
                () -> new BusinessException(ErrorMessages.STATUS_REASON_REQUIRED)
        );


        task.setStatus(request.status());

        Task updatedTask = taskRepository.save(task);

        taskProgressService.logProgress(task, request.status(), request.reason());

        return taskMapper.toDto(updatedTask);
    }

    public void delete(Long taskId) {

        softDelete(taskId);
    }

    public TaskResponseDto updateTask(Long taskId, TaskRequestDto request) {

        Task task = findById(taskId);

        //AssignedUser değişikliği var mı
        Long incomingUserId = request.assignedUserId();
        Long currentUserId = task.getAssignedUser() != null ? task.getAssignedUser().getId() : null;

        if (incomingUserId != null && !incomingUserId.equals(currentUserId)) {
            User newAssignedUser = userRepository.findById(incomingUserId)
                    .orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.USER_NOT_FOUND));

            boolean isAssigned = projectUserAssignmentRepository
                    .findByProjectIdAndUserId(task.getProject().getId(), newAssignedUser.getId())
                    .isPresent();

            ExceptionHandler.throwIf(!isAssigned, () -> new BusinessException(ErrorMessages.USER_NOT_ASSIGNED));

            task.setAssignedUser(newAssignedUser);
        }

        //diğer alanları güncelle
        task.setTitle(request.title());
        task.setDescription(request.description());
        task.setAcceptanceCriteria(request.acceptanceCriteria());
        task.setPriority(request.priority());
        // kaydet
        Task updatedTask = taskRepository.save(task);
        // dto dön
        return taskMapper.toDto(updatedTask);
    }



}
