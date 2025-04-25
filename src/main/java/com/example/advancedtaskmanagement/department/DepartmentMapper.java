package com.example.advancedtaskmanagement.department;

import com.example.advancedtaskmanagement.project.Project;
import com.example.advancedtaskmanagement.user.User;
import org.springframework.stereotype.Component;

@Component
public class DepartmentMapper {

    public DepartmentResponseDto toDepartmentResponseDto(Department department) {

        return new DepartmentResponseDto(
                department.getId(),
                department.getName(),
                department.getUserList().stream().map(User::getId).toList(),
                department.getProjects().stream().map(Project::getId).toList()
        );
    }

}
