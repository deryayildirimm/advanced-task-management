package com.example.advancedtaskmanagement.task.task_attachment;

import com.example.advancedtaskmanagement.task.Task;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskAttachment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    private String filePath;
    private String fileName;
    private String fileType;
    private LocalDateTime uploadedAt;


    @ManyToOne
    @JoinColumn(name = "task_id")
    private Task task;


}
