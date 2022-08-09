package com.dnd.niceteam.project;

import com.dnd.niceteam.domain.project.ProjectType;
import com.dnd.niceteam.project.dto.ProjectRequest;

import java.time.LocalDate;

public class ProjectTestFactory {

    private static final Long id = 1L;
    private static final String name = "테스트 프로젝트";
    private static final ProjectType type = ProjectType.MY_LECTURE;
    private static final LocalDate start_date = LocalDate.of(2022, 7, 4);
    private static final LocalDate end_date = LocalDate.of(2022, 8, 27);

    public static ProjectRequest.Register createRegisterRequest() {
        ProjectRequest.Register request = new ProjectRequest.Register();
        request.setName(name);
        request.setType(type);
        request.setStartDate(start_date);
        request.setEndDate(end_date);
        return request;
    }

}
