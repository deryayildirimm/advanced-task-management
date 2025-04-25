package com.example.advancedtaskmanagement.task.task_progress;

import com.example.advancedtaskmanagement.common.BaseEntity;
import com.example.advancedtaskmanagement.task.TaskStatus;
import com.example.advancedtaskmanagement.task.Task;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class TaskProgress extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "task_id")
    private Task task;

    @Enumerated(EnumType.STRING)
    private TaskStatus status;

    private String reason;
    private LocalDateTime changedAt;

}
