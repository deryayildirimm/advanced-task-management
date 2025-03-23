package com.example.advancedtaskmanagement.task.task_attachment;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface TaskAttachmentRepository  extends JpaRepository<TaskAttachment, Long> {

    List<TaskAttachment> findByTaskId(Long id);

}
