package com.example.advancedtaskmanagement.task;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TaskRequestDto {


    private final String title;
    private final String description;
    private final TaskPriority priority;
    private final TaskStatus status;
    private final Long projectId; // Hangi proje için oluşturuluyor?
    private final Long assignedUserId; // Kime atanıyor?

}
