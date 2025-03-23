package com.example.advancedtaskmanagement.task.task_progress;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TaskProgressRepository extends JpaRepository<TaskProgress, Integer> {

    Optional<TaskProgress> findByIdAndIsDeletedFalse(Long id);
}
