package com.dnd.niceteam.comment;

import com.dnd.niceteam.comment.dto.CommentCreation;
import com.dnd.niceteam.domain.code.*;
import com.dnd.niceteam.project.dto.LectureTimeRequest;
import com.dnd.niceteam.recruiting.dto.RecruitingCreation;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;

public class DtoFactoryForTest {
    public static final String COMMENT_CONTENT = "모집글의 댓글입니다.";
    public static final Long PARENT_ID = 0L;
    public static final Long RECRUITING_ID = 1L;
    public static final Long PROJECT_ID = 1L;
    public static final Long DEPARTMENT_ID = 1L;

    public static CommentCreation.RequestDto createCommentRequest() {
        CommentCreation.RequestDto dto = new CommentCreation.RequestDto();
        dto.setContent(COMMENT_CONTENT);
        dto.setParentId(PARENT_ID);
        return dto;
    }

    public static final Long COMMENT_ID = 1L;
    public static CommentCreation.ResponseDto createCommentResponse() {
        CommentCreation.ResponseDto dto = new CommentCreation.ResponseDto();
        dto.setId(COMMENT_ID);
        return dto;
    }

    public static RecruitingCreation.RequestDto createLectureRecruitingRequest() {
        RecruitingCreation.RequestDto dto = new RecruitingCreation.RequestDto();
        dto.setTitle("모집글 제목 테스트");
        dto.setContent("모집글 내용 테스트");
        dto.setRecruitingType(Type.LECTURE);
//        dto.setStatus(ProgressStatus.IN_PROGRESS);
        dto.setRecruitingEndDate(LocalDate.of(2022, 8, 16));
        dto.setActivityArea(ActivityArea.ONLINE);
        dto.setRecruitingMemberCount(3);
        dto.setIntroLink("test-intro");
        dto.setPersonalityAdjectives(Set.of(Personality.Adjective.PRECISE, Personality.Adjective.CREATIVE));
        dto.setPersonalityNouns(Set.of(Personality.Noun.INVENTOR, Personality.Noun.MEDIATOR));

        dto.setProjectStartDate(LocalDate.of(2022, 7, 4));
        dto.setProjectEndDate(LocalDate.of(2022, 8, 28));

        dto.setProjectName("project-name");
        dto.setDepartmentId(DEPARTMENT_ID);
        dto.setProfessor("test-professor");
        dto.setLectureTimes(createLectureTimeRequest());
        return dto;
    }

    private static List<LectureTimeRequest> createLectureTimeRequest() {
        LectureTimeRequest request1 = new LectureTimeRequest();
        request1.setDayOfWeek(DayOfWeek.MON);
        request1.setStartTime(LocalTime.of(9,0));

        LectureTimeRequest request2 = new LectureTimeRequest();
        request2.setDayOfWeek(DayOfWeek.WED);
        request2.setStartTime(LocalTime.of(9,30));
        return List.of(request1, request2);
    }

    public static RecruitingCreation.ResponseDto createLectureRecruitingResponse() {
        RecruitingCreation.ResponseDto dto = new RecruitingCreation.ResponseDto();
        dto.setRecruitingId(RECRUITING_ID);
        dto.setProjectId(PROJECT_ID);
        return dto;
    }
}
