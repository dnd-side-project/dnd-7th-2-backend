package com.dnd.niceteam.project.service;

import com.dnd.niceteam.domain.code.Type;
import com.dnd.niceteam.domain.department.Department;
import com.dnd.niceteam.domain.department.DepartmentRepository;
import com.dnd.niceteam.domain.member.Member;
import com.dnd.niceteam.domain.member.MemberRepository;
import com.dnd.niceteam.domain.project.*;
import com.dnd.niceteam.domain.recruiting.Applicant;
import com.dnd.niceteam.domain.recruiting.ApplicantRepository;
import com.dnd.niceteam.domain.recruiting.Recruiting;
import com.dnd.niceteam.domain.recruiting.RecruitingRepository;
import com.dnd.niceteam.error.exception.ErrorCode;
import com.dnd.niceteam.project.ProjectTestFactory;
import com.dnd.niceteam.project.dto.DepartmentResponse;
import com.dnd.niceteam.project.dto.ProjectMemberRequest;
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
import org.springframework.security.core.userdetails.User;

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
    MemberRepository memberRepository;
    @Mock
    LectureProjectRepository lectureProjectRepository;
    @Mock
    SideProjectRepository sideProjectRepository;
    @Mock
    DepartmentRepository departmentRepository;
    @Mock
    ProjectMemberRepository projectMemberRepository;
    @Mock
    RecruitingRepository recruitingRepository;
    @Mock
    ApplicantRepository applicantRepository;

    @DisplayName("신규 강의 프로젝트 등록")
    @Test
    void registerLectureProject() {
        // given
        ProjectRequest.Register request = mock(ProjectRequest.Register.class);
        when(request.getType()).thenReturn(Type.LECTURE);
        when(request.getStartDate()).thenReturn(LocalDate.now().plusDays(1));
        when(request.getEndDate()).thenReturn(LocalDate.now().plusDays(2));
        Member member = ProjectTestFactory.createMember();

        Department department = mock(Department.class, RETURNS_DEEP_STUBS);
        when(departmentRepository.findById(anyLong())).thenReturn(Optional.of(department));

        LectureProject newLectureProject = mock(LectureProject.class);
        when(newLectureProject.getType()).thenReturn(Type.LECTURE);
        when(lectureProjectRepository.save(any())).thenReturn(newLectureProject);

        try (MockedStatic<DepartmentResponse> mockedDepartmentResponse = mockStatic(DepartmentResponse.class, RETURNS_DEEP_STUBS)) {
            DepartmentResponse departmentResponse = mock(DepartmentResponse.class, RETURNS_DEEP_STUBS);
            mockedDepartmentResponse.when(() -> DepartmentResponse.from(any())).thenReturn(departmentResponse);

            // when
            ProjectResponse.Detail response = projectService.registerProject(request, member);

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
        Member member = ProjectTestFactory.createMember();
        SideProject newSideProject = mock(SideProject.class);

        // when
        when(sideProjectRepository.save(any(SideProject.class))).thenReturn(newSideProject);
        ProjectResponse.Detail response = projectService.registerProject(request, member);

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
        Member member = ProjectTestFactory.createMember();

        LocalDate startDate = request.getStartDate();
        request.setEndDate(startDate.minusDays(1));

        // when
        InvalidProjectSchedule exception = assertThrows(
                InvalidProjectSchedule.class,
                () -> projectService.registerProject(request, member)
        );

        // then
        assertAll(
                () -> assertEquals(exception.getErrorCode().getStatus(), SC_BAD_REQUEST),
                () -> assertEquals(exception.getErrorCode().getMessage(), ErrorCode.INVALID_PROJECT_SCHEDULE.getMessage())
        );
    }

    @DisplayName("팀플 팀원 등록")
    @Test
    void addProjectMember() {
        // given
        ProjectMemberRequest.Add request = ProjectTestFactory.createProjectMemberAddRequest();
        User currentUser = ProjectTestFactory.createCurrentUser();

        Member member = ProjectTestFactory.createMember();
        when(memberRepository.findByEmail(anyString())).thenReturn(Optional.of(member));

        Recruiting recruiting = mock(Recruiting.class);
        Project project = mock(Project.class);
        when(recruitingRepository.findById(anyLong())).thenReturn(Optional.of(recruiting));
        when(recruiting.getProject()).thenReturn(project);
        when(project.getId()).thenReturn(1L);
        when(recruiting.checkRecruiter(any())).thenReturn(true);

        Applicant applicant = mock(Applicant.class);
        when(applicantRepository.findByMemberIdAndRecruitingId(anyLong(), anyLong())).thenReturn(Optional.of(applicant));

        // when
        projectService.addProjectMember(request, currentUser);

        // then
        verify(projectMemberRepository).save(any(ProjectMember.class));
    }

}