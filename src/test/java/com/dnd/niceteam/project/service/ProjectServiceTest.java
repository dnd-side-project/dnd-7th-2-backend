package com.dnd.niceteam.project.service;

import com.dnd.niceteam.domain.code.Type;
import com.dnd.niceteam.domain.department.Department;
import com.dnd.niceteam.domain.department.DepartmentRepository;
import com.dnd.niceteam.domain.project.LectureProject;
import com.dnd.niceteam.domain.project.LectureProjectRepository;
import com.dnd.niceteam.domain.project.SideProject;
import com.dnd.niceteam.domain.project.SideProjectRepository;
import com.dnd.niceteam.error.exception.ErrorCode;
import com.dnd.niceteam.project.ProjectTestFactory;
import com.dnd.niceteam.project.dto.DepartmentResponse;
import com.dnd.niceteam.project.dto.ProjectRequest;
import com.dnd.niceteam.project.dto.ProjectResponse;
import com.dnd.niceteam.project.exception.InvalidProjectSchedule;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static javax.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProjectServiceTest {

    @InjectMocks
    ProjectService projectService;

    @Mock
    LectureProjectRepository lectureProjectRepository;
    @Mock
    SideProjectRepository sideProjectRepository;
    @Mock
    DepartmentRepository departmentRepository;

    @DisplayName("신규 강의 프로젝트 등록")
    @Test
    void registerLectureProject() {
        // given
        ProjectRequest.Register request = ProjectTestFactory.createRegisterRequest(Type.LECTURE);

        Department department = mock(Department.class, RETURNS_DEEP_STUBS);
        when(departmentRepository.findById(anyLong())).thenReturn(Optional.of(department));

        LectureProject newLectureProject = request.toLectureProject(department);
        when(lectureProjectRepository.save(any(LectureProject.class))).thenReturn(newLectureProject);

        try (MockedStatic<DepartmentResponse> mockedDepartmentResponse = mockStatic(DepartmentResponse.class, RETURNS_DEEP_STUBS)) {
            DepartmentResponse departmentResponse = mock(DepartmentResponse.class, RETURNS_DEEP_STUBS);
            mockedDepartmentResponse.when(() -> DepartmentResponse.from(any())).thenReturn(departmentResponse);

            // when
            ProjectResponse.Detail response = projectService.registerProject(request);

            // then
            assertAll(
                    () -> assertEquals(response.getId(), newLectureProject.getId()),

                    // 프로젝트 상태
                    () -> assertEquals(response.getName(), newLectureProject.getName()),
                    () -> assertEquals(response.getStatus(), newLectureProject.getStatus()),

                    () -> assertEquals(response.getStartDate(), newLectureProject.getStartDate()),
                    () -> assertEquals(response.getEndDate(), newLectureProject.getEndDate()),

                    // 강의 프로젝트 상태
                    () -> assertEquals(response.getProfessor(), newLectureProject.getProfessor())
            );
        }
    }

    @DisplayName("신규 사이드 프로젝트 등록")
    @Test
    void registerSideProject() {
        // given
        ProjectRequest.Register request = ProjectTestFactory.createRegisterRequest(Type.SIDE);
        SideProject newSideProject = request.toSideProject();

        // when
        when(sideProjectRepository.save(any(SideProject.class))).thenReturn(newSideProject);
        ProjectResponse.Detail response = projectService.registerProject(request);

        // then
        assertAll(
                () -> assertEquals(response.getId(), newSideProject.getId()),

                // 프로젝트 상태
                () -> assertEquals(response.getName(), newSideProject.getName()),
                () -> assertEquals(response.getStatus(), newSideProject.getStatus()),

                () -> assertEquals(response.getStartDate(), newSideProject.getStartDate()),
                () -> assertEquals(response.getEndDate(), newSideProject.getEndDate()),

                // 사이드 프로젝트 상태
                () -> assertEquals(response.getField(), newSideProject.getField()),
                () -> assertEquals(response.getFieldCategory(), newSideProject.getFieldCategory())
        );
    }

    @DisplayName("프로젝트 등록 실패 : 종료일이 시작일보다 빠른 경우")
    @Test
    void checkProjectScheduleWhenRegistering() {
        // given
        ProjectRequest.Register request = ProjectTestFactory.createRegisterRequest(Type.LECTURE);

        LocalDate startDate = request.getStartDate();
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