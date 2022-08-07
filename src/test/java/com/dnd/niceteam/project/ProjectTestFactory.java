package com.dnd.niceteam.project;

import com.dnd.niceteam.domain.project.Project;
import com.dnd.niceteam.domain.project.ProjectStatus;
import com.dnd.niceteam.domain.project.ProjectType;
import com.dnd.niceteam.project.dto.ProjectRequest;

import java.time.LocalDateTime;

public class ProjectTestFactory {

    private static final Long id = 1L;
    private static final String name = "테스트 프로젝트";
    private static final ProjectType type = ProjectType.MY_LECTURE;
    private static final LocalDateTime startDate = LocalDateTime.of(2022, 7, 4, 0, 0);
    private static final LocalDateTime endDate = LocalDateTime.of(2022, 8, 27, 23, 59);

    public static ProjectRequest.Register createRegisterRequest() {
        ProjectRequest.Register request = new ProjectRequest.Register();
        request.setName(name);
        request.setType(type);
        request.setStartDate(startDate);
        request.setEndDate(endDate);
        return request;
    }

}
