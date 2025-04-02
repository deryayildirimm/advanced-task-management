package com.example.advancedtaskmanagement.project;

import java.util.Date;

public record ProjectResponseDto (
        Long id,
        String title,
        String description,
        Date startDate,
        Date endDate,
        ProjectStatus status,
        String departmentName
){ }
