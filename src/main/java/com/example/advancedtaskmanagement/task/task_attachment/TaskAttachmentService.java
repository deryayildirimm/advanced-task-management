package com.example.advancedtaskmanagement.task.task_attachment;

import org.springframework.stereotype.Service;

@Service
public class TaskAttachmentService {

    private final TaskAttachmentRepository repository;

    public TaskAttachmentService(TaskAttachmentRepository repository) {
        this.repository = repository;
    }
}
