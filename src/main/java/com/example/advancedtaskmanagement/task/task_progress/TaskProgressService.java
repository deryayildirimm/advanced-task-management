package com.example.advancedtaskmanagement.task.task_progress;

import com.example.advancedtaskmanagement.task.Task;
import com.example.advancedtaskmanagement.task.TaskRepository;
import com.example.advancedtaskmanagement.task.TaskResponseDto;
import com.example.advancedtaskmanagement.task.TaskStatus;
import com.example.advancedtaskmanagement.task.task_comment.TaskComment;
import com.example.advancedtaskmanagement.task.task_comment.TaskCommentRequestDto;
import com.example.advancedtaskmanagement.task.task_comment.TaskCommentResponseDto;
import com.example.advancedtaskmanagement.user.User;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskProgressService {

    private final TaskProgressRepository repository;
    private final TaskProgressMapper mapper;
    private final TaskRepository taskRepository;
    private final TaskProgressRepository taskProgressRepository;

    public TaskProgressService(
            TaskProgressRepository repository,
            TaskProgressMapper mapper, TaskRepository taskRepository, TaskProgressRepository taskProgressRepository) {
        this.repository = repository;
        this.mapper = mapper;
        this.taskRepository = taskRepository;
        this.taskProgressRepository = taskProgressRepository;
    }

    public TaskProgressResponseDto addProgress(TaskProgressRequestDto request) {
        Task task = taskRepository.findById(request.getTaskId()).orElseThrow(EntityNotFoundException::new);

        TaskProgress progress = TaskProgress.builder()
                .task(task)
                .changedAt(new Date())
                .reason(request.getReason())
                .build();

        return mapper.toResponseDto(taskProgressRepository.save(progress));
    }

    public List<TaskProgressResponseDto> getById(Long id) {
        return repository.findByIdAndIsDeletedFalse(id).stream()
                .map(mapper::toResponseDto)
                .collect(Collectors.toList());
    }
    // TaskProgress kaydını oluşturma (geçmiş durumları saklamak için)
    public TaskProgressResponseDto  addTaskProgress(Task task, TaskStatus status, String reason) {
        TaskProgress taskProgress = new TaskProgress();
        taskProgress.setTask(task);
        taskProgress.setStatus(status);
        taskProgress.setReason(reason);
        taskProgress.setChangedAt(new Date());

        // TaskProgress'i kaydet
        taskProgress = repository.save(taskProgress);

        // TaskProgress'i DTO'ya dönüştürme ve geri döndürme
        return mapper.toResponseDto(taskProgress);
    }
}
