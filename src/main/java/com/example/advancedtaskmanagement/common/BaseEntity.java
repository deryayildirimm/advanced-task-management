package com.example.advancedtaskmanagement.common;

import jakarta.persistence.*;
import lombok.Data;
import java.util.Date;

@Data
@MappedSuperclass
public class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // ID'nin otomatik artırılmasını sağlar
    private Long id;

    @Column
    private boolean isDeleted = false;

    @Column
    private Date deletedAt;

    @Column
    private Long deletedBy;


}
