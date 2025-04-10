package com.example.advancedtaskmanagement.task.task_attachment;

import com.example.advancedtaskmanagement.common.BaseEntity;
import com.example.advancedtaskmanagement.task.Task;
import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class TaskAttachment extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "task_id")
    private Task task;

    private String filePath;

}
