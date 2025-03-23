package com.example.advancedtaskmanagement.task.task_attachment;

import com.example.advancedtaskmanagement.task.Task;
import com.example.advancedtaskmanagement.task.TaskRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
public class TaskAttachmentService {

    private final TaskAttachmentRepository repository;
    private final TaskRepository taskRepository;
    private final TaskAttachmentMapper taskAttachmentMapper;


    private final String uploadDir = "file-storage";


    public TaskAttachmentService(TaskAttachmentRepository repository, TaskRepository taskRepository, TaskAttachmentMapper taskAttachmentMapper) {
        this.repository = repository;
        this.taskRepository = taskRepository;
        this.taskAttachmentMapper = taskAttachmentMapper;
    }

    public TaskAttachmentResponseDto uploadAttachment(Long taskId, MultipartFile file) throws IOException {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));


        File dir = new File(uploadDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        File destination = new File(dir, fileName);
        file.transferTo(destination);

        TaskAttachment attachment = TaskAttachment.builder()
                .task(task)
                .filePath(destination.getAbsolutePath())
                .build();

        return taskAttachmentMapper.toDto(repository.save(attachment));
    }

    public List<TaskAttachmentResponseDto> getAttachmentsByTaskId(Long taskId) {
        return repository.findByTaskId(taskId)
                .stream()
                .map(taskAttachmentMapper::toDto)
                .toList();
    }

    public void deleteAttachment(Long attachmentId) {
        TaskAttachment attachment = repository.findById(attachmentId)
                .orElseThrow(() -> new RuntimeException("Attachment not found"));


        File file = new File(attachment.getFilePath());
        if (file.exists()) {
            file.delete();
        }

        repository.delete(attachment);
    }

}
