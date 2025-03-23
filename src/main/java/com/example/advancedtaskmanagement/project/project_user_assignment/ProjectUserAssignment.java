package com.example.advancedtaskmanagement.project.project_user_assignment;


import com.example.advancedtaskmanagement.common.BaseEntity;
import com.example.advancedtaskmanagement.project.Project;
import com.example.advancedtaskmanagement.user.Role;
import com.example.advancedtaskmanagement.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectUserAssignment extends BaseEntity {


    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    private Role role;

}
