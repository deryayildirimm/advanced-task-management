package com.example.advancedtaskmanagement.task.task_progress;

import com.example.advancedtaskmanagement.task.Task;
import com.example.advancedtaskmanagement.task.TaskStatus;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class TaskProgressService {

    private final TaskProgressRepository repository;
    private final TaskProgressMapper mapper;

    public TaskProgressService(
            TaskProgressRepository repository,
            TaskProgressMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
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
