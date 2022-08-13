package com.dnd.niceteam.project.service;

import com.dnd.niceteam.domain.department.Department;
import com.dnd.niceteam.domain.department.DepartmentRepository;
import com.dnd.niceteam.domain.project.LectureProject;
import com.dnd.niceteam.domain.project.ProjectRepository;
import com.dnd.niceteam.error.exception.ErrorCode;
import com.dnd.niceteam.project.LectureProjectTestFactory;
import com.dnd.niceteam.project.dto.LectureProjectRequest;
import com.dnd.niceteam.project.dto.LectureProjectResponse;
import com.dnd.niceteam.project.exception.InvalidProjectSchedule;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static javax.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LectureProjectServiceTest {

    @InjectMocks
    LectureProjectService lectureProjectService;

    @Mock
    ProjectRepository projectRepository;
    @Mock
    DepartmentRepository departmentRepository;

    @DisplayName("신규 강의 프로젝트를 등록한 뒤 저장한 상태값들을 반환합니다.")
    @Test
    void registerProject() {
        // given
        LectureProjectRequest.Register request = LectureProjectTestFactory.createRegisterRequest();
        LectureProject newLectureProject = mock(LectureProject.class, RETURNS_DEEP_STUBS);

        Department department = mock(Department.class);
        when(departmentRepository.findById(anyLong())).thenReturn(Optional.of(department));

        // when
        when(projectRepository.save(any(LectureProject.class))).thenReturn(newLectureProject);
        LectureProjectResponse.Detail response = lectureProjectService.registerLectureProject(request);

        // then
        assertAll(
                () -> assertEquals(response.getId(), newLectureProject.getId()),

                // 프로젝트 상태
                () -> assertEquals(response.getName(), newLectureProject.getName()),
                () -> assertEquals(response.getStatus(), newLectureProject.getStatus()),

                () -> assertEquals(response.getStartDate(), newLectureProject.getStartDate()),
                () -> assertEquals(response.getEndDate(), newLectureProject.getEndDate())
        );
    }

    @DisplayName("프로젝트 종료일이 시작일보다 빠를 경우 Bad Request를 던집니다.")
    @Test
    void checkProjectScheduleWhenRegistering() {
        // given
        LectureProjectRequest.Register request = LectureProjectTestFactory.createRegisterRequest();

        LocalDate startDate = request.getStartDate();
        request.setEndDate(startDate.minusDays(1));

        // when
        InvalidProjectSchedule exception = assertThrows(
                InvalidProjectSchedule.class,
                () -> lectureProjectService.registerLectureProject(request)
        );

        // then
        assertAll(
                () -> assertEquals(exception.getErrorCode().getStatus(), SC_BAD_REQUEST),
                () -> assertEquals(exception.getErrorCode().getMessage(), ErrorCode.INVALID_PROJECT_SCHEDULE.getMessage())
        );
    }

}