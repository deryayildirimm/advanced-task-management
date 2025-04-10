package com.example.advancedtaskmanagement.task.task_progress;

import com.example.advancedtaskmanagement.task.Task;
import com.example.advancedtaskmanagement.task.TaskRepository;
import com.example.advancedtaskmanagement.task.TaskStatus;
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


    protected TaskProgress getTaskProgressById(Long id) {
        return taskProgressRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }


    public TaskProgressResponseDto addProgress(TaskProgressRequestDto request) {
        Task task = taskRepository.findById(request.taskId()).orElseThrow(EntityNotFoundException::new);

        TaskProgress progress = TaskProgress.builder()
                .task(task)
                .changedAt(new Date())
                .reason(request.reason())
                .build();

        return mapper.toResponseDto(taskProgressRepository.save(progress));
    }

    public List<TaskProgressResponseDto> getById(Long id) {
        return repository.findByIdAndIsDeletedFalse(id).stream()
                .map(mapper::toResponseDto)
                .collect(Collectors.toList());
    }

    public TaskProgressResponseDto  addTaskProgress(Task task, TaskStatus status, String reason) {
        TaskProgress taskProgress = new TaskProgress();
        taskProgress.setTask(task);
        taskProgress.setStatus(status);
        taskProgress.setReason(reason);
        taskProgress.setChangedAt(new Date());


        taskProgress = repository.save(taskProgress);

        return mapper.toResponseDto(taskProgress);
    }
}
