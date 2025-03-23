package com.example.advancedtaskmanagement.task;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByIsDeletedFalse();

    Optional<Task> findByIdAndIsDeletedFalse(Long id);

    List<Task> findByProjectIdAndIsDeletedFalse(Long projectId);

    List<Task> findByAssignedUserIdAndIsDeletedFalse(Long userId);
}
