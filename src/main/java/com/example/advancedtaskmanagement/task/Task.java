package com.example.advancedtaskmanagement.task;

import com.example.advancedtaskmanagement.common.BaseEntity;
import com.example.advancedtaskmanagement.project.Project;
import com.example.advancedtaskmanagement.task.task_attachment.TaskAttachment;
import com.example.advancedtaskmanagement.task.task_comment.TaskComment;
import com.example.advancedtaskmanagement.task.task_progress.TaskProgress;
import com.example.advancedtaskmanagement.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
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

    // orphanRemoval  -> Task’tan çıkarılan dosyalar veritabanından da silinir
    @OneToMany(mappedBy = "task" , cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TaskAttachment> attachments;


}
