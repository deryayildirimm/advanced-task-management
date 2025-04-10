package com.example.advancedtaskmanagement.project.project_user_assignment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProjectUserAssignmentRepository extends JpaRepository<ProjectUserAssignment, Long> {

    List<ProjectUserAssignment> findAllByIsDeletedFalse();

    List<ProjectUserAssignment> findByProjectIdAndIsDeletedFalse(Long projectId);

    Optional<ProjectUserAssignment> findByProjectIdAndUserId(Long projectId, Long userId);


    @Query("SELECT pua FROM ProjectUserAssignment pua " +
            "JOIN FETCH pua.user " +
            "WHERE pua.project.id = :projectId")
    List<ProjectUserAssignment> findByProjectIdWithUser(@Param("projectId") Long projectId);
}
