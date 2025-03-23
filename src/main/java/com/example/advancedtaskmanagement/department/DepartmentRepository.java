package com.example.advancedtaskmanagement.department;

import com.example.advancedtaskmanagement.project.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DepartmentRepository extends JpaRepository<Department, Long> {

    @Query("SELECT p FROM Project p WHERE p.department.id = :departmentId")
    List<Project> findProjectsByDepartmentId(Long departmentId);

}
