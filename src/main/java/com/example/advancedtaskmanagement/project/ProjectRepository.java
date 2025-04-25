package com.example.advancedtaskmanagement.project;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    Optional<Project> findByTitle(String title);

    List<Project> findByIsDeletedFalse();

    @Query(value = "SELECT p FROM Project p WHERE (:title IS NULL OR p.title LIKE %:title%)" +
            " AND (:status IS NULL OR p.status = :status)" +
            " AND (:departmentId IS NULL OR p.department.id = :departmentId)" +
            " AND p.isDeleted = false")
    List<Project> filterProjects(@Param("title") String title,
                                 @Param("status") ProjectStatus status,
                                 @Param("departmentId") Long departmentId);


}
