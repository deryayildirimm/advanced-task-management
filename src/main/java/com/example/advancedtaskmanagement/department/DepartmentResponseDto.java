package com.example.advancedtaskmanagement.department;

import com.example.advancedtaskmanagement.project.ProjectResponseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class DepartmentResponseDto {

    private Long id;
    private String name;
    private List<Long> projectIds;
    private List<Long> userIds;

    /*
    Evet, şu an için ID formatında bırakmak en mantıklısı.
    ✔ Performans açısından daha iyi olur.
    ✔ İlk aşamada gereksiz veri yükünü önleriz.
    ✔ Eğer detayları çekmek istersek, ayrı bir API endpoint yazarız.
     */

}
