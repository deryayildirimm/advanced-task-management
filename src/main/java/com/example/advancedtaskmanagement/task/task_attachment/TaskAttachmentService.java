package com.example.advancedtaskmanagement.task.task_attachment;

import com.example.advancedtaskmanagement.common.ErrorMessages;
import com.example.advancedtaskmanagement.exception.FileStorageException;
import com.example.advancedtaskmanagement.exception.ResourceNotFoundException;
import com.example.advancedtaskmanagement.task.Task;
import com.example.advancedtaskmanagement.task.TaskService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class TaskAttachmentService {

    private final TaskAttachmentRepository attachmentRepository;
    private final TaskService taskService;
    private final TaskAttachmentMapper taskAttachmentMapper;


    public TaskAttachmentService(TaskAttachmentRepository attachmentRepository,
                                 TaskService taskService,
                                 TaskAttachmentMapper taskAttachmentMapper) {
        this.attachmentRepository = attachmentRepository;
        this.taskService = taskService;
        this.taskAttachmentMapper = taskAttachmentMapper;
    }

    private final String uploadDir = "uploads";

    private final Path uploadDirPath = Paths.get(uploadDir);

    public TaskAttachmentResponseDto uploadAttachment(Long taskId, MultipartFile file) {
        Task task = taskService.findById(taskId);

        try{
            if (!Files.exists(uploadDirPath)) {
                Files.createDirectory(uploadDirPath);
            }

            String uniqueFileName = UUID.randomUUID() + "_" +file.getOriginalFilename();
            Path filePath = uploadDirPath.resolve(uniqueFileName);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            TaskAttachment taskAttachment = TaskAttachment.builder()
                    .fileName(file.getOriginalFilename())
                    .task(task)
                    .filePath(filePath.toString())
                    .uploadedAt(LocalDateTime.now())
                    .build();

           TaskAttachment savedTaskAttachment = attachmentRepository.save(taskAttachment);

            return taskAttachmentMapper.toDto(savedTaskAttachment);
        }catch(IOException e){
            throw new FileStorageException(ErrorMessages.FILE_UPLOAD_FAILED, e);
        }
    }


    protected TaskAttachment findTaskAttachmentById(Long id){
        return attachmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.TASK_ATTACHMENT_NOT_FOUND));
    }


    public List<TaskAttachmentResponseDto> getAttachmentsByTaskId(Long taskId) {
        return attachmentRepository.findByTaskId(taskId)
                .stream()
                .map(taskAttachmentMapper::toDto)
                .toList();
    }

    public void deleteAttachment(Long attachmentId) {
        TaskAttachment attachment = findTaskAttachmentById(attachmentId);

        try{
            Files.deleteIfExists(Path.of(attachment.getFilePath()));
        }catch (IOException ex){
            throw  new FileStorageException(ErrorMessages.FILE_DELETE_FAILED, ex);
        }

        attachmentRepository.delete(attachment);
    }

}
