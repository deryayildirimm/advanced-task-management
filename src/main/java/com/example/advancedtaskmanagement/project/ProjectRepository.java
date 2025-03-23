package com.example.advancedtaskmanagement.project;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    Optional<Project> findByTitle(String title);

    List<Project> findByIsDeletedFalse();

    @Query(value = "SELECT * FROM project p WHERE (:title IS NULL OR p.title LIKE %:title%)" +
            " AND (:status IS NULL OR p.status = :status)" +
            " AND (:departmentId IS NULL OR p.department_id = :departmentId)" +
            " AND (:startDate IS NULL OR p.start_date >= :startDate)" +
            " AND (:endDate IS NULL OR p.end_date <= :endDate)" +
            " AND p.is_deleted = false",
            nativeQuery = true)
    List<Project> filterProjects(@Param("title") String title,
                                 @Param("status") String status,
                                 @Param("departmentId") Long departmentId,
                                 @Param("startDate") Date startDate,
                                 @Param("endDate") Date endDate);


}
