package com.example.advancedtaskmanagement.task.task_attachment;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("v1/attachments")
public class TaskAttachmentController {

    private final TaskAttachmentService taskAttachmentService;

    public TaskAttachmentController(TaskAttachmentService taskAttachmentService) {
        this.taskAttachmentService = taskAttachmentService;
    }

    @PostMapping("/task/{taskId}")
    public ResponseEntity<TaskAttachmentResponseDto> addAttachmentToTask(
            @PathVariable("taskId") Long taskId,
            @RequestParam("file") MultipartFile file) throws IOException {

        return ResponseEntity.status(HttpStatus.CREATED).body(taskAttachmentService.uploadAttachment(taskId,file));
    }

    @GetMapping("/task/{taskId}")
    public ResponseEntity<List<TaskAttachmentResponseDto>> getAttachmentsByTaskId(@PathVariable Long taskId) {
        return ResponseEntity.ok(taskAttachmentService.getAttachmentsByTaskId(taskId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAttachment(@PathVariable Long id) {
        taskAttachmentService.deleteAttachment(id);
        return ResponseEntity.noContent().build();
    }
}
