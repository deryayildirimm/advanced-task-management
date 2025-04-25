package com.example.advancedtaskmanagement.task.task_comment;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/task-comments")
public class TaskCommentController {

    private final TaskCommentService taskCommentService;

    public TaskCommentController(TaskCommentService taskCommentService) {
        this.taskCommentService = taskCommentService;
    }

    @PostMapping
    public ResponseEntity<TaskCommentResponseDto> addCommentToTask(@RequestBody @Valid TaskCommentRequestDto request) {
        return ResponseEntity.ok(taskCommentService.addComment(request));
    }

    @GetMapping("/task/{taskId}")
    public ResponseEntity<List<TaskCommentResponseDto>> getCommentsByTaskId(@PathVariable Long taskId) {
        return ResponseEntity.ok(taskCommentService.getCommentsByTaskId(taskId));
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId) {
        taskCommentService.deleteComment(commentId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<TaskCommentResponseDto> updateComment(
            @PathVariable Long commentId,
            @RequestBody @Valid TaskCommentRequestDto request) {

        return ResponseEntity.ok(taskCommentService.updateComment(commentId, request));
    }
}
