package com.example.advancedtaskmanagement.task;

import com.example.advancedtaskmanagement.common.BaseEntity;
import com.example.advancedtaskmanagement.project.Project;
import com.example.advancedtaskmanagement.task.task_comment.TaskComment;
import com.example.advancedtaskmanagement.task.task_progress.TaskProgress;
import com.example.advancedtaskmanagement.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Task extends BaseEntity {


    private String title;
    private String description;

    private String acceptanceCriteria;

    @Enumerated(EnumType.STRING)
    private TaskPriority priority;

    @Enumerated(EnumType.STRING)
    private TaskStatus status;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    @ManyToOne
    @JoinColumn(name = "assigned_user_id")
    private User assignedUser;

    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL)
    private List<TaskProgress> progressHistory;

    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL)
    private List<TaskComment> comments;


}
