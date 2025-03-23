package com.example.advancedtaskmanagement.task.task_attachment;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v1/task-attachments")
public class TaskAttachmentController {

    /*
    Bu entity, bir göreve ait dosya eklerini temsil ediyor.
    **
    Dosyaları gerçek anlamda yüklemek için MultipartFile ile @RequestParam
     veya @ModelAttribute kullanman gerekebilir.
      Ancak şu an DTO üzerinden String filePath gönderdiğimiz için @RequestBody ile ilerliyoruz.
     */

    private final TaskAttachmentService taskAttachmentService;

    public TaskAttachmentController(TaskAttachmentService taskAttachmentService) {
        this.taskAttachmentService = taskAttachmentService;
    }

    @PostMapping
    public ResponseEntity<TaskAttachmentResponseDto> addAttachmentToTask(@RequestBody TaskAttachmentRequestDto request) {
        // TODO: implement
        return null;
    }

    @GetMapping("/task/{taskId}")
    public ResponseEntity<List<TaskAttachmentResponseDto>> getAttachmentsByTaskId(@PathVariable Long taskId) {
        // TODO: implement
        return null;
    }

    @DeleteMapping("/{attachmentId}")
    public ResponseEntity<Void> deleteAttachment(@PathVariable Long attachmentId) {
        // TODO: implement
        return null;
    }
}
