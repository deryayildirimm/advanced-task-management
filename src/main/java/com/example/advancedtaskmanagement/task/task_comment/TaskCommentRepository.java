package com.example.advancedtaskmanagement.task.task_comment;

import com.example.advancedtaskmanagement.task.task_attachment.TaskAttachment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface TaskCommentRepository extends JpaRepository<TaskComment, Long> {

    /*
    Ne zaman List<T> kullanılır?
    Birden fazla sonuç döndüren sorgular için (findByTaskId(Long taskId)).
    Boş bir liste döndürmek mantıklıysa (Collections.emptyList()).
     */
    Optional<TaskComment> findByTaskId(Long taskId);
}
