package com.example.advancedtaskmanagement.task.task_comment;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/task-comments")
public class TaskCommentController {

    /*
    Metot	Açıklama
    POST /api/task-comments	Bir göreve yorum ekler.
    GET /api/task-comments/task/{taskId}	Bir göreve ait tüm yorumları getirir.
    DELETE /api/task-comments/{commentId}	Belirli bir yorumu siler.
    PUT /api/task-comments/{commentId}	Yorumu günceller.
     */

    private final TaskCommentService taskCommentService;

    public TaskCommentController(TaskCommentService taskCommentService) {
        this.taskCommentService = taskCommentService;
    }

    @PostMapping
    public ResponseEntity<TaskCommentResponseDto> addCommentToTask(@RequestBody TaskCommentRequestDto request) {
        // TODO: implement
        return null;
    }

    @GetMapping("/task/{taskId}")
    public ResponseEntity<List<TaskCommentResponseDto>> getCommentsByTaskId(@PathVariable Long taskId) {
        // TODO: implement
        return null;
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId) {
        // TODO: implement
        return null;
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<TaskCommentResponseDto> updateComment(
            @PathVariable Long commentId,
            @RequestBody TaskCommentRequestDto request) {
        // TODO: implement
        return null;
    }
}
