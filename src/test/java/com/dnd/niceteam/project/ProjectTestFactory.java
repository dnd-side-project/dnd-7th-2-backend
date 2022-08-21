package com.dnd.niceteam.project;

import com.dnd.niceteam.domain.code.DayOfWeek;
import com.dnd.niceteam.domain.code.Field;
import com.dnd.niceteam.domain.code.FieldCategory;
import com.dnd.niceteam.domain.code.Type;
import com.dnd.niceteam.domain.member.Member;
import com.dnd.niceteam.project.dto.LectureTimeRequest;
import com.dnd.niceteam.project.dto.ProjectMemberRequest;
import com.dnd.niceteam.project.dto.ProjectRequest;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class ProjectTestFactory {

    private static final Long RECRUITING_ID = 1L;
    private static final Long APPLICANT_MEMBER_ID = 2L;
    private static final Long DEPARTMENT_ID = 1L;

    private static final String EMAIL = "user@dnd.ac.kr";
    private static final String USER_PASSWORD = "Aaaa1111";

    private static final String NAME = "테스트 강의 프로젝트";
    private static final LocalDate START_DATE = LocalDate.of(2022, 7, 4);
    private static final LocalDate END_DATE = LocalDate.of(2022, 8, 27);
    
    private static final String PROFESSOR = "양경호 교수";

    public static ProjectRequest.Register createRegisterRequest(Type type) {
        LectureTimeRequest lectureTimeRequest = createLectureTimeRequest(DayOfWeek.MON, LocalTime.of(8, 0));

        ProjectRequest.Register request = new ProjectRequest.Register();

        request.setName(NAME);
        request.setType(type);
        request.setStartDate(START_DATE);
        request.setEndDate(END_DATE);

        if (type == Type.LECTURE) {
            request.setProfessor(PROFESSOR);
            request.setDepartmentId(DEPARTMENT_ID);
            request.setLectureTimes(List.of(lectureTimeRequest));
        } else {
            request.setField(Field.AD_MARKETING);
            request.setFieldCategory(FieldCategory.STUDY);
        }

        return request;
    }

    public static LectureTimeRequest createLectureTimeRequest(DayOfWeek dayOfWeek, LocalTime startTime) {
        LectureTimeRequest request = new LectureTimeRequest();
        request.setDayOfWeek(dayOfWeek);
        request.setStartTime(startTime);

        return request;
    }

    public static Member createMember() {
        return Member.builder()
                .id(1L)
                .build();
    }

    public static ProjectMemberRequest.Add createProjectMemberAddRequest() {
        ProjectMemberRequest.Add request = new ProjectMemberRequest.Add();

        request.setApplicantMemberId(APPLICANT_MEMBER_ID);
        request.setRecruitingId(RECRUITING_ID);

        return request;
    }

    public static User createCurrentUser() {
        return (User) User.withUsername(EMAIL)
                .password(USER_PASSWORD)
                .authorities(AuthorityUtils.NO_AUTHORITIES)
                .build();
    }

}
