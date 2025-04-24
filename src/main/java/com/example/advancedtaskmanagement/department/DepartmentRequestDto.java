package com.example.advancedtaskmanagement.department;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record DepartmentRequestDto (

        @NotBlank(message = "Department name must not be blank")
        @Size(min = 1, max = 50, message = "Department name must be between 1 and 50 characters")
        String name
) { }
