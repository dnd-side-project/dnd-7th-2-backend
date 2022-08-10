package com.dnd.niceteam.project.dto;

import com.dnd.niceteam.domain.department.Department;
import lombok.Data;

@Data
public class DepartmentResponse {

    private Long id;

    private UniversityResponse university;

    private String collegeName;

    private String name;

    private String mainBranchType;

    private String region;

    public static DepartmentResponse from(Department department) {
        DepartmentResponse dto = new DepartmentResponse();

        dto.setId(department.getId());

        dto.setUniversity(UniversityResponse.from(department.getUniversity()));

        dto.setCollegeName(department.getCollegeName());
        dto.setName(department.getName());
        dto.setMainBranchType(department.getMainBranchType());
        dto.setRegion(department.getRegion());

        return dto;
    }

}
