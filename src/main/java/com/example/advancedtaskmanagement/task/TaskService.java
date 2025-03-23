package com.example.advancedtaskmanagement.task;

import com.example.advancedtaskmanagement.task.task_progress.TaskProgress;
import com.example.advancedtaskmanagement.task.task_progress.TaskProgressRepository;
import com.example.advancedtaskmanagement.task.task_progress.TaskProgressService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final TaskProgressService taskProgressService;
    private final TaskMapper taskMapper;


    public TaskService(
            TaskRepository taskRepository,
            TaskProgressService taskProgressService,
            TaskMapper taskMapper
                       ) {
        this.taskRepository = taskRepository;
        this.taskProgressService = taskProgressService;
        this.taskMapper = taskMapper;
    }

    // Task status güncelleme
    public TaskResponseDto updateTaskStatus(Long taskId, TaskStatus status, String reason) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new EntityNotFoundException("Task not found"));

        task.setStatus(status);

        // Eğer task zaten completed durumdaysa, başka bir duruma geçmesine izin verilmez
        if (task.getStatus() == TaskStatus.COMPLETED) {
            throw new IllegalArgumentException("Completed tasks cannot be moved to any other state");
        }

        // Durum geçişi kontrolü (cancelled veya blocked durumları için)
        if ((status == TaskStatus.CANCELLED || status == TaskStatus.BLOCKED) &&
                (task.getStatus() != TaskStatus.IN_ANALYSIS && task.getStatus() != TaskStatus.IN_PROGRESS)) {
            throw new IllegalArgumentException("Invalid transition to Cancelled or Blocked");
        }

        // Durum geçişi kontrolü (Cancelled veya Blocked için reason gereklidir)
        if ((status == TaskStatus.CANCELLED || status == TaskStatus.BLOCKED) && (reason == null || reason.isEmpty())) {
            throw new IllegalArgumentException("Reason must be provided for Cancelled or Blocked status");
        }
        // Durum güncelleme
        task.setStatus(status);

        // Task'ı kaydetme
        Task updatedTask = taskRepository.save(task);

        taskProgressService.addTaskProgress(task, status, reason);

        // TaskResponseDto'ya dönüştür
        return taskMapper.toTaskResponseDto(updatedTask);
    }


}
