package com.example.advancedtaskmanagement.project;

import java.time.LocalDate;

public record ProjectResponseDto (
        Long id,
        String title,
        String description,
        LocalDate startDate,
        LocalDate endDate,
        ProjectStatus status,
        String departmentName
){ }
