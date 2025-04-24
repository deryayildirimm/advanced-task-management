package com.example.advancedtaskmanagement.project;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.Date;



public record ProjectRequestDto(
         @NotBlank(message = "Project title must not be blank")
         @Size(max = 100, message = "Project title must be at most 100 characters")
         String title,
         @Size(max = 500, message = "Project description must be at most 500 characters")
         String description,
         @NotNull
         @FutureOrPresent(message = "Start date cannot be in the past")
         LocalDate startDate ,
         LocalDate endDate,
         @NotNull
         ProjectStatus status,
         @NotNull
         Long departmentId
) { }
