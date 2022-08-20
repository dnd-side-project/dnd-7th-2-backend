package com.dnd.niceteam.project;

import com.dnd.niceteam.domain.code.DayOfWeek;
import com.dnd.niceteam.domain.code.Field;
import com.dnd.niceteam.domain.code.FieldCategory;
import com.dnd.niceteam.domain.code.Type;
import com.dnd.niceteam.domain.member.Member;
import com.dnd.niceteam.project.dto.LectureTimeRequest;
import com.dnd.niceteam.project.dto.ProjectRequest;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class ProjectTestFactory {

    private static final Long ID = 1L;
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
            request.setDepartmentId(1L);
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

}
