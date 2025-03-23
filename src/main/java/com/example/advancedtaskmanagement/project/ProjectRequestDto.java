package com.example.advancedtaskmanagement.project;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Date;


@Getter
@AllArgsConstructor
public class ProjectRequestDto {

    private String title;
    private String description;
    private Date startDate;
    private Date endDate;
    private ProjectStatus status;
    private Long departmentId;  // Projenin bağlı olduğu departman


}
