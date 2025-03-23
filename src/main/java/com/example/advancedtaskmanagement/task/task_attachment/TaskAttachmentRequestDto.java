package com.example.advancedtaskmanagement.task.task_attachment;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TaskAttachmentRequestDto {

    private Long taskId;
    private String filePath;

}
