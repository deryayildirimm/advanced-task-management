package com.example.advancedtaskmanagement.department;

import com.example.advancedtaskmanagement.common.BaseEntity;
import com.example.advancedtaskmanagement.project.Project;
import com.example.advancedtaskmanagement.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Department  extends BaseEntity {

    private String name;

    @OneToMany(mappedBy = "department", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Project> projects;

    @OneToMany(mappedBy = "department" , fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<User> userList;
}
