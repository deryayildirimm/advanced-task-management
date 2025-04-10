package com.example.advancedtaskmanagement.department;

import java.util.List;


public record DepartmentResponseDto (
        Long id,
        String name,
        List<Long> projectIds,
        List<Long> userIds

) { }
