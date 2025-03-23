package com.example.advancedtaskmanagement.task.task_attachment;

import com.example.advancedtaskmanagement.common.BaseEntity;
import com.example.advancedtaskmanagement.task.Task;
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
public class TaskAttachment extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "task_id")
    private Task task;

    private String filePath;

}
