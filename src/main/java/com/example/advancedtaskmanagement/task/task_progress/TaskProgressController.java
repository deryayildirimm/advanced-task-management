package com.example.advancedtaskmanagement.task.task_progress;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/task-progress")
public class TaskProgressController {

    private final TaskProgressService taskProgressService;

    public TaskProgressController(TaskProgressService taskProgressService) {
        this.taskProgressService = taskProgressService;
    }


    @PostMapping
    public ResponseEntity<TaskProgressResponseDto> addProgressToTask(@RequestBody TaskProgressRequestDto request) {
       TaskProgressResponseDto taskProgressResponseDto = taskProgressService.addProgress(request);
        return ResponseEntity.ok(taskProgressResponseDto);
    }

    @GetMapping("/task/{taskId}")
    public ResponseEntity<List<TaskProgressResponseDto>> getProgressByTaskId(@PathVariable Long taskId) {
        return ResponseEntity.ok(taskProgressService.getById(taskId));
    }


}
