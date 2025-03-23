package com.example.advancedtaskmanagement.task;


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
        // TODO: implement
        return null;
    }

    @GetMapping
    public ResponseEntity<List<TaskResponseDto>> getAllTasks() {
        // TODO: implement
        return null;
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponseDto> getTaskById(@PathVariable Long id) {
        // TODO: implement
        return null;
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskResponseDto> updateTask(@PathVariable Long id, @RequestBody TaskRequestDto request) {
        // TODO: implement
        return null;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        // TODO: implement
        return null;
    }

    // 🔹 Belirli bir projeye ait task'ları getir
    @GetMapping("/project/{projectId}")
    public ResponseEntity<List<TaskResponseDto>> getTasksByProjectId(@PathVariable Long projectId) {
        // TODO: implement
        return null;
    }

    // 🔹 Belirli bir kullanıcıya atanmış task'ları getir
    @GetMapping("/assignee/{userId}")
    public ResponseEntity<List<TaskResponseDto>> getTasksByAssignee(@PathVariable Long userId) {
        // TODO: implement
        return null;
    }

    // 🔹 Bir task'ın sadece statüsünü güncelle
    @PatchMapping("/{taskId}/status")
    public ResponseEntity<TaskResponseDto> changeTaskStatus(@PathVariable Long taskId, @RequestBody TaskStatus status) {
        // TODO: implement
        return null;
    }
}
