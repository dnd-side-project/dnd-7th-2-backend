package com.dnd.niceteam.university.dto;

import com.dnd.niceteam.domain.department.Department;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DepartmentDto {

    private Long id;

    private String collegeName;

    private String name;

    public static DepartmentDto of(Department department) {
        return new DepartmentDto(department.getId(), department.getCollegeName(), department.getName());
    }
}
