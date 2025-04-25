package com.example.advancedtaskmanagement.department;

import com.example.advancedtaskmanagement.project.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface DepartmentRepository extends JpaRepository<Department, Long> {

    /*
    Optional<Department> sadece tek bir kayıt dönecek sorgularda kullanılır.
    (örneğin findById(Long id) veya findByTitle(String title) gibi).
    List<Department> ise birden fazla kayıt dönecek yerlerde kullanılır.
     */
    @Query("SELECT p FROM Project p WHERE p.department.id = :departmentId")
    List<Project> findProjectsByDepartmentId(Long departmentId);

    Optional<Department> findByNameIgnoreCase(String name);
    List<Department> findByIsDeletedFalse();

}
