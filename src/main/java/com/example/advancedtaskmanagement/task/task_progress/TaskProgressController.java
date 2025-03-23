package com.example.advancedtaskmanagement.task.task_progress;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/task-progress")
public class TaskProgressController {

    /*
    TaskProgress bir görevin durum değişim geçmişini tuttuğu için
    burada genelde oluşturma, göreve ait tüm ilerlemeleri listeleme gibi
    işlemler yeterli olabilir.
     */

    private final TaskProgressService taskProgressService;

    public TaskProgressController(TaskProgressService taskProgressService) {
        this.taskProgressService = taskProgressService;
    }

    @PostMapping
    public ResponseEntity<TaskProgressResponseDto> addProgressToTask(@RequestBody TaskProgressRequestDto request) {
        // TODO: implement
        return null;
    }

    @GetMapping("/task/{taskId}")
    public ResponseEntity<List<TaskProgressResponseDto>> getProgressByTaskId(@PathVariable Long taskId) {
        // TODO: implement
        return null;
    }

    @DeleteMapping("/{progressId}")
    public ResponseEntity<Void> deleteProgress(@PathVariable Long progressId) {
        // TODO: implement
        return null;
    }

}
