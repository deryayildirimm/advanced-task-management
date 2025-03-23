package com.example.advancedtaskmanagement.task.task_attachment;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("v1/task-attachments")
public class TaskAttachmentController {

    private final TaskAttachmentService taskAttachmentService;

    public TaskAttachmentController(TaskAttachmentService taskAttachmentService) {
        this.taskAttachmentService = taskAttachmentService;
    }

    @PostMapping
    public ResponseEntity<TaskAttachmentResponseDto> addAttachmentToTask(
            @RequestParam("taskId") Long taskId,
            @RequestParam("file") MultipartFile file
    ) throws IOException {
        return ResponseEntity.ok(taskAttachmentService.uploadAttachment(taskId, file));
    }

    @GetMapping("/task/{taskId}")
    public ResponseEntity<List<TaskAttachmentResponseDto>> getAttachmentsByTaskId(@PathVariable Long taskId) {
        return ResponseEntity.ok(taskAttachmentService.getAttachmentsByTaskId(taskId));
    }

    @DeleteMapping("/{attachmentId}")
    public ResponseEntity<Void> deleteAttachment(@PathVariable Long attachmentId) {
        taskAttachmentService.deleteAttachment(attachmentId);
        return ResponseEntity.noContent().build();
    }
}
