package com.example.advancedtaskmanagement.project;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Date;



public record ProjectRequestDto(
        @NotBlank
         String title,
         String description,
         @NotNull
         Date startDate ,
         Date endDate,
         @NotNull
         ProjectStatus status,
         @NotNull
         Long departmentId
) { }
