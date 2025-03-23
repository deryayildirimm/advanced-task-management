package com.example.advancedtaskmanagement.project.project_user_assignment;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProjectUserAssignmentRepository extends JpaRepository<ProjectUserAssignment, Long> {

    List<ProjectUserAssignment> findAllByIsDeletedFalse();
    List<ProjectUserAssignment> findByProjectIdAndIsDeletedFalse(Long projectId);
    Optional<ProjectUserAssignment> findByIdAndIsDeletedFalse(Long id);
}
