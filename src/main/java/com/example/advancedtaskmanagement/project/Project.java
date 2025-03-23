package com.example.advancedtaskmanagement.project;

import com.example.advancedtaskmanagement.common.BaseEntity;
import com.example.advancedtaskmanagement.department.Department;
import com.example.advancedtaskmanagement.task.Task;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;


@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Project extends BaseEntity {

    private String title;
    private String description;


    private Date startDate;
    private Date endDate;

    @Enumerated(EnumType.STRING)
    private ProjectStatus status;

    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    private List<Task> tasks;

}
