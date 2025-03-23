package com.example.advancedtaskmanagement.project;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Date;

@Getter
@AllArgsConstructor
public class ProjectResponseDto {

    private Long id;
    private String title;
    private String description;
    private Date startDate;
    private Date endDate;
    private ProjectStatus status;
    private String departmentName;  // Departman adı
}
