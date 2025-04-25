package com.example.advancedtaskmanagement.task.task_progress;

import com.example.advancedtaskmanagement.task.Task;
import com.example.advancedtaskmanagement.task.TaskRepository;
import com.example.advancedtaskmanagement.task.TaskStatus;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskProgressService {

    private final TaskRepository taskRepository;
    private final TaskProgressRepository taskProgressRepository;
    private final TaskProgressMapper taskProgressMapper;

    public TaskProgressService(
            TaskRepository taskRepository,
            TaskProgressRepository taskProgressRepository, TaskProgressMapper taskProgressMapper) {
        this.taskRepository = taskRepository;

        this.taskProgressRepository = taskProgressRepository;
        this.taskProgressMapper = taskProgressMapper;
    }


    protected TaskProgress getTaskProgressById(Long id) {
        return taskProgressRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    public void logProgress(Task task, TaskStatus status, String reason) {

        TaskProgress taskProgress = TaskProgress.builder()
                .task(task)
                .status(status)
                .reason(reason)
                .changedAt(LocalDateTime.now())
                .build();
        taskProgressRepository.save(taskProgress);
    }


    public TaskProgressResponseDto addProgress(TaskProgressRequestDto request) {
        Task task = taskRepository.findById(request.taskId()).orElseThrow(EntityNotFoundException::new);

        TaskProgress progress = TaskProgress.builder()
                .task(task)
                .changedAt(LocalDateTime.now())
                .reason(request.reason())
                .build();

        return taskProgressMapper.toDto(taskProgressRepository.save(progress));
    }

    public List<TaskProgressResponseDto> getById(Long id) {
        return taskProgressRepository.findByIdAndIsDeletedFalse(id).stream()
                .map(taskProgressMapper::toDto)
                .collect(Collectors.toList());
    }

}
