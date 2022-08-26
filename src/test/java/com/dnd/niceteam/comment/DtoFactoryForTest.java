package com.dnd.niceteam.comment;

import com.dnd.niceteam.comment.dto.CommentCreation;
import com.dnd.niceteam.comment.dto.CommentFind;
import com.dnd.niceteam.domain.code.*;
import com.dnd.niceteam.domain.recruiting.RecruitingStatus;
import com.dnd.niceteam.project.dto.LectureTimeRequest;
import com.dnd.niceteam.project.dto.ProjectResponse;
import com.dnd.niceteam.recruiting.dto.ActivityDayTimeDto;
import com.dnd.niceteam.recruiting.dto.RecruitingCreation;
import com.dnd.niceteam.recruiting.dto.RecruitingFind;
import com.dnd.niceteam.recruiting.dto.RecruitingModify;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;

public class DtoFactoryForTest {
    public static final String COMMENT_CONTENT = "모집글의 댓글입니다.";
    public static final Long RECRUITING_ID = 1L;
    public static final Long PROJECT_ID = 1L;
    public static final Long DEPARTMENT_ID = 1L;
    public static final int PAGE = 1;
    public static final int PER_SIZE = 5;
    public static final Long COMMENT_ID = 1L;
    public static final Long PARENT_ID = 0L;
    public static final Long GROUP_NO = 1L;

    public static CommentCreation.RequestDto createCommentAddRequest() {
        CommentCreation.RequestDto dto = new CommentCreation.RequestDto();
        dto.setContent(COMMENT_CONTENT);
        dto.setParentId(PARENT_ID);
        return dto;
    }

    public static CommentCreation.ResponseDto createCommentAddResponse() {
        CommentCreation.ResponseDto dto = new CommentCreation.ResponseDto();
        dto.setId(COMMENT_ID);
        dto.setParentId(PARENT_ID);
        dto.setGroupNo(GROUP_NO);
        return dto;
    }

    public static CommentFind.ResponseDto createCommentListResponse() {
        CommentFind.ResponseDto dto = new CommentFind.ResponseDto();
        dto.setCommentId(1L);
        dto.setParentId(0L);
        dto.setContent("모집글의 모댓글입니다.");
        dto.setRecruitingId(RECRUITING_ID);
        dto.setCreatedAt(LocalDateTime.now());
        return dto;
    }

    public static RecruitingCreation.RequestDto createLectureRecruitingRequest() {
        RecruitingCreation.RequestDto dto = new RecruitingCreation.RequestDto();
        dto.setTitle("모집글 제목 테스트");
        dto.setContent("모집글 내용 테스트");
        dto.setRecruitingType(Type.LECTURE);
        dto.setRecruitingEndDate(LocalDate.of(2023, 8, 28));
        dto.setActivityArea(ActivityArea.ONLINE);
        dto.setStatus(RecruitingStatus.IN_PROGRESS);
        dto.setRecruitingMemberCount(3);
        dto.setIntroLink("test-intro");
        dto.setPersonalityAdjectives(Set.of(Personality.Adjective.PRECISE, Personality.Adjective.CREATIVE));
        dto.setPersonalityNouns(Set.of(Personality.Noun.INVENTOR, Personality.Noun.MEDIATOR));
        dto.setActivityDayTimes(createActivityDayTimesDto());

        dto.setProjectStartDate(LocalDate.of(2023, 8, 29));
        dto.setProjectEndDate(LocalDate.of(2023, 12, 28));

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

    public static RecruitingFind.DetailResponseDto createDetailSideRecruitingResponse() {
        RecruitingFind.DetailResponseDto dto = new RecruitingFind.DetailResponseDto();
        dto.setTitle("모집글 제목 테스트");
        dto.setContent("모집글 내용 테스트");
        dto.setRecruitingType(Type.SIDE);
        dto.setRecruitingStatus(RecruitingStatus.IN_PROGRESS);
        dto.setRecruitingEndDate(LocalDate.of(2022, 8, 16));
        dto.setActivityArea(ActivityArea.ONLINE);
        dto.setRecruitingMemberCount(3);
        dto.setIntroLink("test-intro");
        dto.setPersonalityAdjectives(Set.of(Personality.Adjective.PRECISE, Personality.Adjective.CHEERFUL));
        dto.setPersonalityNouns(Set.of(Personality.Noun.INVENTOR, Personality.Noun.JACK_OF_ALL_TRADES));
        dto.setActivityDayTimes(createActivityDayTimesDto());
        dto.setIsBookmarked(Boolean.FALSE);

        ProjectResponse.Detail detailProject = new ProjectResponse.Detail();
        detailProject.setStartDate(LocalDate.of(2022, 7, 4));
        detailProject.setEndDate(LocalDate.of(2022, 8, 28));
        detailProject.setName("project-name");
        detailProject.setField(Field.IT_SW_GAME);
        detailProject.setFieldCategory(FieldCategory.STUDY);

        dto.setProjectResponse(detailProject);
        return dto;
    }

    public static RecruitingFind.ListResponseDto createListLectureRecruitingResponse() {
        RecruitingFind.ListResponseDto dto = new RecruitingFind.ListResponseDto();
        dto.setId(RECRUITING_ID);
        dto.setTitle("모집글 제목 테스트");
        dto.setType(Type.LECTURE);
        dto.setStatus(RecruitingStatus.IN_PROGRESS);
        dto.setCommentCount(0);
        dto.setBookmarkCount(0);
        dto.setCreatedDate(LocalDateTime.of(2022,8,21,13,0));
        dto.setProjectName("test-lecture-project-name");
        dto.setProfessor("test-professor-name");
        return dto;
    }

    public static RecruitingFind.RecommendedListResponseDto createRecommendedSideRecruitingListResponse() {
        RecruitingFind.RecommendedListResponseDto dto = new RecruitingFind.RecommendedListResponseDto();
        dto.setRecruitingId(RECRUITING_ID);
        dto.setTitle("모집글 제목 테스트");
        dto.setRecruitingMemberCount(4);
        dto.setProjectStartDate(LocalDate.of(2022,7,4));
        dto.setProjectEndDate(LocalDate.of(2022,8,27));
        dto.setRecruitingEndDate(LocalDate.of(2022,7,3));

        dto.setField(Field.AD_MARKETING);
        dto.setFieldCategory(FieldCategory.CONTEST);
        return dto;
    }

    public static Set<ActivityDayTimeDto> createActivityDayTimesDto() {
        ActivityDayTimeDto dto = new ActivityDayTimeDto();
        dto.setDayOfWeek(DayOfWeek.SAT);
        dto.setStartTime(LocalTime.of(17, 30));
        dto.setEndTime(LocalTime.of(20, 30));

        return Set.of(dto);
    }

    public static RecruitingFind.ListResponseDto createSearchSideListResponseDto() {
        RecruitingFind.ListResponseDto responseDto = new RecruitingFind.ListResponseDto();
        responseDto.setId(1L);
        responseDto.setRecruiterNickname("writer-tester");
        responseDto.setRecruitingMemberCount(4);
        responseDto.setRecruitingEndDate(LocalDate.of(2022,8,28));
        responseDto.setStatus(RecruitingStatus.IN_PROGRESS);
        responseDto.setTitle("모집글 제목 테스트");
        responseDto.setProjectName("프르젝트명 테스트");
        responseDto.setBookmarkCount(2);
        responseDto.setCommentCount(2);
        responseDto.setType(Type.SIDE);
        responseDto.setField(Field.IT_SW_GAME);
        responseDto.setFieldCategory(FieldCategory.STUDY);
        return responseDto;
    }

    public static RecruitingModify.RequestDto createRecruitingModifyRequest() {
        RecruitingModify.RequestDto dto = new RecruitingModify.RequestDto();
        dto.setTitle("모집글 제목 테스트");
        dto.setContent("모집글 내용 테스트");
        dto.setRecruitingType(Type.LECTURE);
        dto.setRecruitingEndDate(LocalDate.of(2022, 8, 16));
        dto.setActivityArea(ActivityArea.ONLINE);
        dto.setRecruitingMemberCount(3);
        dto.setIntroLink("test-intro");
        dto.setPersonalityAdjectives(Set.of(Personality.Adjective.PRECISE, Personality.Adjective.CREATIVE));
        dto.setPersonalityNouns(Set.of(Personality.Noun.INVENTOR, Personality.Noun.MEDIATOR));
        dto.setActivityDayTimes(createActivityDayTimesDto());

        dto.setProjectStartDate(LocalDate.of(2022, 7, 4));
        dto.setProjectEndDate(LocalDate.of(2022, 8, 28));

        dto.setProjectName("project-name");
        dto.setDepartmentId(DEPARTMENT_ID);
        dto.setProfessor("test-professor");
        dto.setLectureTimes(createLectureTimeRequest());

        if (dto.getRecruitingType() == Type.LECTURE) {
            dto.setDepartmentId(DEPARTMENT_ID);
            dto.setProfessor("test-professor");
            dto.setLectureTimes(createLectureTimeRequest());
        } else {
            dto.setField(Field.AD_MARKETING);
            dto.setFieldCategory(FieldCategory.STUDY);
        }

        return dto;
    }
}
