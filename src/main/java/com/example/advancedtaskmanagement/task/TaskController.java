package com.example.advancedtaskmanagement.task;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    public ResponseEntity<TaskResponseDto> createTask(@RequestBody TaskRequestDto request) {
        return ResponseEntity.ok(taskService.createTask(request));
    }

    @GetMapping
    public ResponseEntity<List<TaskResponseDto>> getAllTasks() {
       List<TaskResponseDto> taskList = taskService.getAll();
        return ResponseEntity.status(HttpStatus.OK).body(taskList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponseDto> getTaskById(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.getById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.delete(id);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/project/{projectId}")
    public ResponseEntity<List<TaskResponseDto>> getTasksByProjectId(@PathVariable Long projectId) {
       List<TaskResponseDto> tasks = taskService.getByProjectId(projectId);
        return ResponseEntity.status(HttpStatus.OK).body(tasks);
    }


    @GetMapping("/assignee/{userId}")
    public ResponseEntity<List<TaskResponseDto>> getTasksByAssignee(@PathVariable Long userId) {

        List<TaskResponseDto> taskList = taskService.getByAssigneeId(userId);
        return ResponseEntity.status(HttpStatus.OK).body(taskList);
    }

    @PatchMapping("/{taskId}/status")
    public ResponseEntity<TaskResponseDto> changeTaskStatus(@PathVariable Long taskId, @RequestBody TaskStatus status , @RequestBody String reason) {
        TaskResponseDto taskResponse = taskService.updateTaskStatus(taskId, status, reason);
        return ResponseEntity.status(HttpStatus.OK).body(taskResponse);
    }
}
