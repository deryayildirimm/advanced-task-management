package com.example.advancedtaskmanagement.task.task_comment;

import com.example.advancedtaskmanagement.common.BaseEntity;
import com.example.advancedtaskmanagement.user.User;
import com.example.advancedtaskmanagement.task.Task;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaskComment extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "task_id")
    private Task task;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String content;
    private Date createdAt;

}
