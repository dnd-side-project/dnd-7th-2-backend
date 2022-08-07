package com.dnd.niceteam.project.service;

import com.dnd.niceteam.domain.project.Project;
import com.dnd.niceteam.domain.project.ProjectRepository;
import com.dnd.niceteam.error.exception.ErrorCode;
import com.dnd.niceteam.project.ProjectTestFactory;
import com.dnd.niceteam.project.dto.ProjectRequest;
import com.dnd.niceteam.project.dto.ProjectResponse;
import com.dnd.niceteam.project.exception.InvalidProjectSchedule;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static javax.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProjectServiceTest {

    @InjectMocks
    ProjectService projectService;

    @Mock
    ProjectRepository projectRepository;

    @DisplayName("신규 프로젝트를 등록한 뒤 저장한 상태값들을 반환합니다.")
    @Test
    void registerProject() {
        // given
        ProjectRequest.Register request = ProjectTestFactory.createRegisterRequest();
        Project newProject = request.toEntity();

        // when
        when(projectRepository.save(any(Project.class))).thenReturn(newProject);
        ProjectResponse.Detail response = projectService.registerProject(request);

        // then
        assertAll(
                () -> assertEquals(response.getId(), newProject.getId()),

                // 프로젝트 상태
                () -> assertEquals(response.getName(), newProject.getName()),
                () -> assertEquals(response.getType(), newProject.getType()),
                () -> assertEquals(response.getStatus(), newProject.getStatus()),

                () -> assertEquals(response.getStartDate(), newProject.getStartDate()),
                () -> assertEquals(response.getEndDate(), newProject.getEndDate())
        );
    }

    @DisplayName("프로젝트 종료일이 시작일보다 빠를 경우 Bad Request를 던집니다.")
    @Test
    void checkProjectScheduleWhenRegistering() {
        // given
        ProjectRequest.Register request = ProjectTestFactory.createRegisterRequest();

        LocalDateTime startDate = request.getStartDate();
        request.setEndDate(startDate.minusDays(1));

        // when
        InvalidProjectSchedule exception = assertThrows(
                InvalidProjectSchedule.class,
                () -> projectService.registerProject(request)
        );

        // then
        assertAll(
                () -> assertEquals(exception.getErrorCode().getStatus(), SC_BAD_REQUEST),
                () -> assertEquals(exception.getErrorCode().getMessage(), ErrorCode.INVALID_PROJECT_SCHEDULE.getMessage())
        );
    }

}