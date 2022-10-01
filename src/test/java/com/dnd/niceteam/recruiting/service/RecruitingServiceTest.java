package com.dnd.niceteam.recruiting.service;

import com.dnd.niceteam.common.dto.Pagination;
import com.dnd.niceteam.domain.bookmark.BookmarkRepository;
import com.dnd.niceteam.domain.code.*;
import com.dnd.niceteam.domain.department.Department;
import com.dnd.niceteam.domain.member.Member;
import com.dnd.niceteam.domain.member.MemberRepository;
import com.dnd.niceteam.domain.project.*;
import com.dnd.niceteam.domain.recruiting.*;
import com.dnd.niceteam.project.dto.*;
import com.dnd.niceteam.project.service.ProjectService;
import com.dnd.niceteam.recruiting.dto.ActivityDayTimeDto;
import com.dnd.niceteam.recruiting.dto.RecruitingCreation;
import com.dnd.niceteam.recruiting.dto.RecruitingFind;
import com.dnd.niceteam.recruiting.dto.RecruitingModify;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RecruitingServiceTest {
    @InjectMocks
    private RecruitingService recruitingService;
    @Mock
    private ProjectService projectService;
    @Mock
    private MemberRepository mockMemberRepository;
    @Mock
    private ProjectRepository mockProjectRepository;
    @Mock
    private RecruitingRepository mockRecruitingRepository;
    @Mock
    private BookmarkRepository mockBookmarkRepository;

    private final String email = "tester@gmail.com";
    private final Long departmentId = 1L;
    private final Long recruitingId = 1L;
    private final Long projectId = 1L;
    private final int page = 1;
    private final int perSize = 5;

    @DisplayName("(Lecture)신규 모집글 작성 서비스 테스트 코드 ")
    @Test
    public void postRecruiting() {
        // given
        Member mockMember = mock(Member.class);
        given(mockMemberRepository.findByEmail(email))
                .willReturn(Optional.of(mockMember));
        LectureProject mockProject = mock(LectureProject.class);

        ProjectResponse.Detail projectResponseDetail = new ProjectResponse.Detail();
        projectResponseDetail.setId(projectId);
        projectResponseDetail.setName(mockProject.getName());
        projectResponseDetail.setStartDate(mockProject.getStartDate());
        projectResponseDetail.setEndDate(mockProject.getEndDate());

        DepartmentResponse departmentResponse = new DepartmentResponse();
        departmentResponse.setId(departmentId);
        projectResponseDetail.setDepartment(departmentResponse);
        projectResponseDetail.setProfessor(mockProject.getProfessor());
        LectureTimeResponse lectureTimeResponse = new LectureTimeResponse();
        lectureTimeResponse.setDayOfWeek(DayOfWeek.FRI);
        lectureTimeResponse.setStartTime(LocalTime.of(9, 0));
        projectResponseDetail.setLectureTimes(List.of(lectureTimeResponse));

        given(projectService.registerProject(any(ProjectRequest.Register.class), any(Member.class)))
                .willReturn(projectResponseDetail);
        given(mockProjectRepository.getReferenceById(projectId)).willReturn(mockProject);

        Recruiting recruiting = Recruiting.builder()
                .id(recruitingId)
                .member(mockMember)
                .project(mockProject)
                .title("recruiting title")
                .content("recruiting content")
                .introLink("intro link")
                .activityArea(ActivityArea.BUSAN)
                .recruitingMemberCount(4)
                .recruitingType(Type.LECTURE)
                .recruitingEndDate(LocalDate.of(2022, 12, 29))
                .status(RecruitingStatus.IN_PROGRESS)
                .commentCount(0)
                .poolUpCount(0)
                .bookmarkCount(0)
                .personalityNouns(Set.of(Personality.Noun.JACK_OF_ALL_TRADES))
                .personalityAdjectives(Set.of(Personality.Adjective.LOGICAL))
                .activityDayTimes(Set.of(ActivityDayTime.builder().
                        dayOfWeek(DayOfWeek.FRI)
                        .startTime(LocalTime.of(17, 0)).endTime(LocalTime.of(20, 0))
                        .build()))
                .build();
        given(mockRecruitingRepository.save(any(Recruiting.class))).willReturn(recruiting);
        RecruitingCreation.RequestDto requestDto = new RecruitingCreation.RequestDto();
        requestDto.setTitle(recruiting.getTitle());
        requestDto.setContent(recruiting.getContent());
        requestDto.setIntroLink(recruiting.getIntroLink());
        requestDto.setActivityArea(recruiting.getActivityArea());
        requestDto.setRecruitingEndDate(recruiting.getRecruitingEndDate());
        requestDto.setStatus(recruiting.getStatus());
        requestDto.setRecruitingMemberCount(recruiting.getRecruitingMemberCount());
        requestDto.setProjectName(recruiting.getProject().getName());
        requestDto.setProjectStartDate(mockProject.getStartDate());
        requestDto.setProjectEndDate(mockProject.getEndDate());
        requestDto.setRecruitingEndDate(mockProject.getEndDate());
        requestDto.setRecruitingType(recruiting.getRecruitingType());
        requestDto.setProfessor(mockProject.getProfessor());
        requestDto.setDepartmentId(departmentId);
        LectureTimeRequest lectureTimeRequest = new LectureTimeRequest();
        lectureTimeRequest.setDayOfWeek(DayOfWeek.WED);
        lectureTimeRequest.setStartTime(LocalTime.of(10, 0));
        requestDto.setLectureTimes(List.of(lectureTimeRequest));
        requestDto.setPersonalityNouns(recruiting.getPersonalityNouns());
        requestDto.setPersonalityAdjectives(recruiting.getPersonalityAdjectives());

        ActivityDayTimeDto activityDayTimeDto = new ActivityDayTimeDto();
        activityDayTimeDto.setDayOfWeek(DayOfWeek.FRI);
        activityDayTimeDto.setStartTime(LocalTime.of(17, 0));
        activityDayTimeDto.setEndTime(LocalTime.of(20, 0));
        requestDto.setActivityDayTimes(Set.of(activityDayTimeDto));

        // when
        RecruitingCreation.ResponseDto responseDto = recruitingService.addProjectAndRecruiting(email, requestDto);

        // then
        verify(mockRecruitingRepository).save(any(Recruiting.class));
        assertThat(responseDto.getRecruitingId()).isEqualTo(recruitingId);
        assertThat(responseDto.getProjectId()).isEqualTo(mockProject.getId());
        assertThat(recruiting.getPoolUpDate()).isNotNull();
    }

    @DisplayName("(Lecture) 모집글 상세 조회 서비스 테스트 코드")
    @Test
    public void detailRecruiting() {
        // given
        Member mockMember = mock(Member.class);
        LectureProject mockProject = mock(LectureProject.class, RETURNS_DEEP_STUBS);    // stub 'Department'
        Recruiting recruiting = Recruiting.builder()
                .id(recruitingId)
                .member(mockMember)
                .project(mockProject)
                .title("recruiting title")
                .content("recruiting content")
                .introLink("intro link")
                .activityArea(ActivityArea.BUSAN)
                .recruitingMemberCount(4)
                .recruitingType(Type.LECTURE)
                .recruitingEndDate(LocalDate.of(2022, 12, 29))
                .status(RecruitingStatus.IN_PROGRESS)
                .commentCount(0)
                .poolUpCount(0)
                .bookmarkCount(0)
                .personalityNouns(Set.of(Personality.Noun.JACK_OF_ALL_TRADES))
                .personalityAdjectives(Set.of(Personality.Adjective.LOGICAL))
                .activityDayTimes(Set.of(ActivityDayTime.builder().
                        dayOfWeek(DayOfWeek.FRI)
                        .startTime(LocalTime.of(17, 0)).endTime(LocalTime.of(20, 0))
                        .build()))
                .build();
        given(mockRecruitingRepository.findById(anyLong())).willReturn(Optional.of(recruiting));
        given(mockMemberRepository.findByEmail(email)).willReturn(Optional.of(mockMember));
        given(mockBookmarkRepository.existsByMemberAndRecruiting(any(Member.class), any(Recruiting.class)))
                .willReturn(Boolean.FALSE);

        //when
        RecruitingFind.DetailResponseDto responseDto = recruitingService.getRecruiting(recruiting.getId(), email);

        // then
        verify(mockRecruitingRepository).findById(anyLong());
        assertThat(responseDto.getRecruitingType()).isEqualTo(recruiting.getRecruitingType());
        assertThat(responseDto.getProjectResponse().getId()).isEqualTo(mockProject.getId());
    }

    @DisplayName("내가 쓴 글 전체 조회 및 상태 별 조회 서비스 테스트 코드")
    @Test
    public void getMyRecruitingsPage() {
        // given
        Member mockMember = mock(Member.class);
        SideProject mockProject = mock(SideProject.class);
        Recruiting recruiting = Recruiting.builder()
                .id(recruitingId)
                .member(mockMember)
                .project(mockProject)
                .title("recruiting title")
                .content("recruiting content")
                .introLink("intro link")
                .activityArea(ActivityArea.BUSAN)
                .recruitingMemberCount(4)
                .recruitingType(Type.SIDE)
                .recruitingEndDate(LocalDate.of(2022, 12, 29))
                .status(RecruitingStatus.IN_PROGRESS)
                .commentCount(0)
                .poolUpCount(0)
                .bookmarkCount(0)
                .personalityNouns(Set.of(Personality.Noun.JACK_OF_ALL_TRADES))
                .personalityAdjectives(Set.of(Personality.Adjective.LOGICAL))
                .activityDayTimes(Set.of(ActivityDayTime.builder().
                        dayOfWeek(DayOfWeek.FRI)
                        .startTime(LocalTime.of(17, 0)).endTime(LocalTime.of(20, 0))
                        .build()))
                .build();

        Pageable pageable = PageRequest.of(page - 1, perSize);
        given(mockMemberRepository.findByEmail(email)).willReturn(Optional.of(mockMember));
        given(mockRecruitingRepository.findPageByMemberOrderByCreatedDateDesc(any(Pageable.class), eq(mockMember)))
                .willReturn(new PageImpl<>(List.of(recruiting), pageable, 1L));
        given(mockRecruitingRepository.findPageByMemberAndStatusOrderByCreatedDateDesc(
                any(Pageable.class), eq(mockMember), any(RecruitingStatus.class)))
                .willReturn(new PageImpl<>(Collections.emptyList(), pageable, 0L));

        //when - 전체 조회
        Pagination<RecruitingFind.ListResponseDto> myRecruitingsPage = recruitingService.getMyRecruitings(
                page, perSize, null, email);
        Pagination<RecruitingFind.ListResponseDto> myRecruitingsPageWithFailed = recruitingService.getMyRecruitings(
                page, perSize, RecruitingStatus.FAILED, email);

        // then
        verify(mockRecruitingRepository).findPageByMemberOrderByCreatedDateDesc(any(Pageable.class), any(Member.class));
        verify(mockRecruitingRepository).findPageByMemberAndStatusOrderByCreatedDateDesc(
                any(Pageable.class), any(Member.class), any(RecruitingStatus.class));
        assertThat(myRecruitingsPage.getContents().get(0).getId()).isEqualTo(recruiting.getId());
        assertThat(myRecruitingsPageWithFailed.getContents().size()).isZero();
    }

    @DisplayName("사이드 모집글 추천 서비스 테스트 코드")
    @Test
    public void getRecommendedSideRecruitingPage() {
        // given
        Member mockMember = mock(Member.class);
        SideProject project = SideProject.builder()
                .name("project name")
                .startDate(LocalDate.of(2022,12,17))
                .endDate(LocalDate.of(2023,3,17))
                .field(Field.IT_SW_GAME)
                .fieldCategory(FieldCategory.STUDY)
                .build();
        Recruiting recruiting = Recruiting.builder()
                .id(recruitingId)
                .member(mockMember)
                .project(project)
                .title("recruiting title")
                .content("recruiting content")
                .introLink("intro link")
                .activityArea(ActivityArea.BUSAN)
                .recruitingMemberCount(4)
                .recruitingType(Type.SIDE)
                .recruitingEndDate(LocalDate.of(2022, 12, 29))
                .status(RecruitingStatus.IN_PROGRESS)
                .commentCount(0)
                .poolUpCount(0)
                .bookmarkCount(0)
                .personalityNouns(Set.of(Personality.Noun.JACK_OF_ALL_TRADES))
                .personalityAdjectives(Set.of(Personality.Adjective.LOGICAL))
                .activityDayTimes(Set.of(ActivityDayTime.builder().
                        dayOfWeek(DayOfWeek.FRI)
                        .startTime(LocalTime.of(17, 0)).endTime(LocalTime.of(20, 0))
                        .build()))
                .build();
        Pageable pageable = PageRequest.of(page - 1, perSize);

        given(mockMemberRepository.findByEmail(email)).willReturn(Optional.of(mockMember));
        given(mockRecruitingRepository.findAllByInterestingFieldsOrderByWriterLevel(
                eq(mockMember.getInterestingFields()), eq(pageable)))
                .willReturn(new PageImpl<>(List.of(recruiting), pageable, 1L));
        //when
        Pagination<RecruitingFind.RecommendedListResponseDto> recommendedRecruitings = recruitingService
                .getRecommendedRecruitings(page, perSize, email);

        // then
        verify(mockRecruitingRepository).findAllByInterestingFieldsOrderByWriterLevel(anySet(), any(Pageable.class));
        assertThat(recommendedRecruitings.getContents().size()).isEqualTo(1);
        assertThat(recommendedRecruitings.getContents().get(0).getRecruitingId()).isEqualTo(recruiting.getId());
    }

    @DisplayName("모집글 검색 - 키워드 & 필터링")
    @Test
    public void getSearchRecruitingWithKeywordPage() {
        // given
        Member mockMember = mock(Member.class, RETURNS_DEEP_STUBS); // stub 'department'
        SideProject mockProject = mock(SideProject.class);
        Recruiting recruiting = Recruiting.builder()
                .id(recruitingId)
                .member(mockMember)
                .project(mockProject)
                .title("DND 동아리원 모집해요!")
                .content("recruiting content")
                .introLink("intro link")
                .activityArea(ActivityArea.BUSAN)
                .recruitingMemberCount(4)
                .recruitingType(Type.SIDE)
                .recruitingEndDate(LocalDate.of(2022, 12, 29))
                .status(RecruitingStatus.IN_PROGRESS)
                .commentCount(0)
                .poolUpCount(0)
                .bookmarkCount(0)
                .personalityNouns(Set.of(Personality.Noun.JACK_OF_ALL_TRADES))
                .personalityAdjectives(Set.of(Personality.Adjective.LOGICAL))
                .activityDayTimes(Set.of(ActivityDayTime.builder().
                        dayOfWeek(DayOfWeek.FRI)
                        .startTime(LocalTime.of(17, 0)).endTime(LocalTime.of(20, 0))
                        .build()))
                .build();

        Pageable pageable = PageRequest.of(page - 1, perSize);
        given(mockMemberRepository.findByEmail(anyString())).willReturn(Optional.of(mockMember));
        given(mockRecruitingRepository.findAllSideBySearchWordAndFieldOrderByCreatedDate(
                anyString(), any(Field.class), any(Pageable.class)))
                .willReturn(new PageImpl<>(List.of(recruiting), pageable, 1L));

        given(mockRecruitingRepository.findAllLectureBySearchWordAndDepartmentOrderByCreatedDate(
                anyString(), anyString(), any(Pageable.class)))
                .willReturn(new PageImpl<>(Collections.emptyList(), pageable, 0L));
        given(mockMember.getDepartment().getName()).willReturn("example-department");

        // when
        Pagination<RecruitingFind.ListResponseDto> searchSideRecruitingPage = recruitingService.getSearchRecruitings(
                page, perSize, Field.IT_SW_GAME, Type.SIDE, "DND", email);  // Side (검색 & 필터링)
        Pagination<RecruitingFind.ListResponseDto> searchLectureRecruitingPage = recruitingService.getSearchRecruitings(
                page, perSize, null, Type.LECTURE, "test-professor" , email); // Lecture (검색 & 필터링)

        // then
        verify(mockRecruitingRepository).findAllSideBySearchWordAndFieldOrderByCreatedDate(anyString(), any(Field.class), any(Pageable.class));
        assertThat(searchSideRecruitingPage.getContents().size()).isEqualTo(1);

        verify(mockRecruitingRepository).findAllLectureBySearchWordAndDepartmentOrderByCreatedDate(anyString(), anyString(), any(Pageable.class));
        assertThat(searchLectureRecruitingPage.getContents().size()).isZero();
    }

    @DisplayName("모집글 검색 - 키워드없이 디폴트")
    @Test
    public void getSearchRecruitingPage() {
        // given
        Department mockDepartment = mock(Department.class);
        Member mockMember = mock(Member.class);
        SideProject mockSideProject = mock(SideProject.class);
        LectureProject mockLectureProject = mock(LectureProject.class);
        Recruiting sideRecruiting = Recruiting.builder()
                .id(recruitingId)
                .member(mockMember)
                .project(mockSideProject)
                .title("DND 동아리원 모집해요!")
                .content("recruiting content")
                .introLink("intro link")
                .activityArea(ActivityArea.BUSAN)
                .recruitingMemberCount(4)
                .recruitingType(Type.SIDE)
                .recruitingEndDate(LocalDate.of(2022, 12, 29))
                .status(RecruitingStatus.IN_PROGRESS)
                .commentCount(0)
                .poolUpCount(0)
                .bookmarkCount(0)
                .personalityNouns(Set.of(Personality.Noun.JACK_OF_ALL_TRADES))
                .personalityAdjectives(Set.of(Personality.Adjective.LOGICAL))
                .activityDayTimes(Set.of(ActivityDayTime.builder().
                        dayOfWeek(DayOfWeek.FRI)
                        .startTime(LocalTime.of(17, 0)).endTime(LocalTime.of(20, 0))
                        .build()))
                .build();
        Pageable pageable = PageRequest.of(page - 1, perSize);
        given(mockMemberRepository.findByEmail(email)).willReturn(Optional.of(mockMember));
        given(mockMember.getDepartment()).willReturn(mockDepartment);
        given(mockMember.getDepartment().getName()).willReturn("different-department");  // 멤버와 동일 department이어야 default 시 조회됨.
        given(mockRecruitingRepository.findAllSideBySearchWordAndFieldOrderByCreatedDate(
                eq(null), eq(null), any(Pageable.class)))
                .willReturn(new PageImpl<>(List.of(sideRecruiting), pageable, 1L));
        given(mockRecruitingRepository.findAllLectureBySearchWordAndDepartmentOrderByCreatedDate(
                eq(null), anyString(), any(Pageable.class)))
                .willReturn(new PageImpl<>(Collections.emptyList(), pageable, 0L));

        // when
        Pagination<RecruitingFind.ListResponseDto> defaultSearchSide = recruitingService.getSearchRecruitings(
                page, perSize, null, Type.SIDE, null , email); // Side (default : 필터 X)
        Pagination<RecruitingFind.ListResponseDto> defaultSearchLecture = recruitingService.getSearchRecruitings(
                page, perSize, null, Type.LECTURE, null, email); // Lecture (default : 전공 학과 필터링)

        // then
        verify(mockRecruitingRepository).findAllLectureBySearchWordAndDepartmentOrderByCreatedDate(
                eq(null), anyString(), any(Pageable.class));
        assertThat(defaultSearchLecture.getContents().size()).isZero();

        verify(mockRecruitingRepository).findAllSideBySearchWordAndFieldOrderByCreatedDate(
                eq(null), eq(null), any(Pageable.class));
        assertThat(defaultSearchSide.getContents().size()).isEqualTo(1);
    }

    @DisplayName("(LECTURE) 모집글 수정 서비스 테스트 코드")
    @Test
    public void modifyRecruiting() {
        // given
        Member mockMember = mock(Member.class);
        SideProject mockProject = mock(SideProject.class);
        Recruiting recruiting = Recruiting.builder()
                .id(recruitingId)
                .member(mockMember)
                .project(mockProject)
                .title("DND 동아리원 모집해요!")
                .content("recruiting content")
                .introLink("intro link")
                .activityArea(ActivityArea.BUSAN)
                .recruitingMemberCount(4)
                .recruitingType(Type.SIDE)
                .recruitingEndDate(LocalDate.of(2022, 12, 29))
                .status(RecruitingStatus.IN_PROGRESS)
                .commentCount(0)
                .poolUpCount(0)
                .bookmarkCount(0)
                .personalityNouns(Set.of(Personality.Noun.JACK_OF_ALL_TRADES))
                .personalityAdjectives(Set.of(Personality.Adjective.LOGICAL))
                .activityDayTimes(Set.of(ActivityDayTime.builder().
                        dayOfWeek(DayOfWeek.FRI)
                        .startTime(LocalTime.of(17, 0)).endTime(LocalTime.of(20, 0))
                        .build()))
                .build();
        RecruitingModify.RequestDto modifyReqDto = new RecruitingModify.RequestDto();
        modifyReqDto.setTitle("updated recruiting title");
        modifyReqDto.setContent(recruiting.getContent());
        modifyReqDto.setIntroLink(recruiting.getIntroLink());
        modifyReqDto.setActivityArea(recruiting.getActivityArea());
        modifyReqDto.setRecruitingEndDate(recruiting.getRecruitingEndDate());
        modifyReqDto.setRecruitingMemberCount(recruiting.getRecruitingMemberCount());
        modifyReqDto.setProjectName(recruiting.getProject().getName());
        modifyReqDto.setProjectStartDate(mockProject.getStartDate());
        modifyReqDto.setProjectEndDate(mockProject.getEndDate());
        modifyReqDto.setRecruitingEndDate(mockProject.getEndDate());
        modifyReqDto.setRecruitingType(recruiting.getRecruitingType());
        modifyReqDto.setDepartmentId(departmentId);
        LectureTimeRequest lectureTimeRequest = new LectureTimeRequest();
        lectureTimeRequest.setDayOfWeek(DayOfWeek.WED);
        lectureTimeRequest.setStartTime(LocalTime.of(10, 0));
        modifyReqDto.setLectureTimes(List.of(lectureTimeRequest));
        modifyReqDto.setPersonalityNouns(recruiting.getPersonalityNouns());
        modifyReqDto.setPersonalityAdjectives(recruiting.getPersonalityAdjectives());

        ActivityDayTimeDto activityDayTimeDto = new ActivityDayTimeDto();
        activityDayTimeDto.setDayOfWeek(DayOfWeek.FRI);
        activityDayTimeDto.setStartTime(LocalTime.of(17, 0));
        activityDayTimeDto.setEndTime(LocalTime.of(20, 0));
        modifyReqDto.setActivityDayTimes(Set.of(activityDayTimeDto));

        given(mockRecruitingRepository.findById(anyLong())).willReturn(Optional.of(recruiting));
        given(mockMemberRepository.findByEmail(email)).willReturn(Optional.of(mockMember));

        // when
        RecruitingModify.ResponseDto recruitingResDto = recruitingService.modifyProjectAndRecruiting(recruitingId, modifyReqDto, email);

        // then
        verify(mockRecruitingRepository).findById(anyLong());
        verify(mockMemberRepository).findByEmail(anyString());
        assertThat(recruitingResDto.getRecruitingId()).isEqualTo(recruiting.getId());
    }

    @DisplayName("모집글 제거 서비스 테스트 코드")
    @Test
    public void removeRecruiting() {
        // given
        Member mockMember = mock(Member.class);
        SideProject project = SideProject.builder()
                .name("project name")
                .startDate(LocalDate.of(2022,12,17))
                .endDate(LocalDate.of(2023,3,17))
                .field(Field.IT_SW_GAME)
                .fieldCategory(FieldCategory.STUDY)
                .build();
        Recruiting recruiting = Recruiting.builder()
                .id(recruitingId)
                .member(mockMember)
                .project(project)
                .title("DND 동아리원 모집해요!")
                .content("recruiting content")
                .introLink("intro link")
                .activityArea(ActivityArea.BUSAN)
                .recruitingMemberCount(4)
                .recruitingType(Type.SIDE)
                .recruitingEndDate(LocalDate.of(2022, 12, 29))
                .status(RecruitingStatus.IN_PROGRESS)
                .commentCount(0)
                .poolUpCount(0)
                .bookmarkCount(0)
                .personalityNouns(Set.of(Personality.Noun.JACK_OF_ALL_TRADES))
                .personalityAdjectives(Set.of(Personality.Adjective.LOGICAL))
                .activityDayTimes(Set.of(ActivityDayTime.builder().
                        dayOfWeek(DayOfWeek.FRI)
                        .startTime(LocalTime.of(17, 0)).endTime(LocalTime.of(20, 0))
                        .build()))
                .build();
        Applicant applicant = Applicant.builder()
                .member(mockMember)
                .recruiting(recruiting)
                .joined(Boolean.FALSE)
                .build();
        recruiting.addApplicant(applicant);
        given(mockRecruitingRepository.findById(anyLong())).willReturn(Optional.of(recruiting));
        given(mockMemberRepository.findByEmail(anyString())).willReturn(Optional.of(mockMember));

        // when
        recruitingService.removeRecruiting(recruitingId, email);

        // then
        verify(mockRecruitingRepository).delete(recruiting);
        assertThat(project.getStatus()).isEqualTo(ProjectStatus.NOT_STARTED);
        verify(mockProjectRepository).delete(project);
        assertThat(recruiting.getApplicants().size()).isZero();
    }

    @DisplayName("모집글 끌올 서비스 테스트 코드")
    @Test
    public void poolupRecruiting() {
        // given
        Member mockMember = mock(Member.class);
        Project mockProject = mock(Project.class);
        Recruiting recruiting = Recruiting.builder()
                .id(recruitingId)
                .member(mockMember)
                .project(mockProject)
                .title("DND 동아리원 모집해요!")
                .content("recruiting content")
                .introLink("intro link")
                .activityArea(ActivityArea.BUSAN)
                .recruitingMemberCount(4)
                .recruitingType(Type.SIDE)
                .recruitingEndDate(LocalDate.of(2022, 12, 29))
                .status(RecruitingStatus.IN_PROGRESS)
                .commentCount(0)
                .poolUpCount(0)
                .bookmarkCount(0)
                .personalityNouns(Set.of(Personality.Noun.JACK_OF_ALL_TRADES))
                .personalityAdjectives(Set.of(Personality.Adjective.LOGICAL))
                .activityDayTimes(Set.of(ActivityDayTime.builder().
                        dayOfWeek(DayOfWeek.FRI)
                        .startTime(LocalTime.of(17, 0)).endTime(LocalTime.of(20, 0))
                        .build()))
                .build();
        RecruitingModify.PoolUpRequestDto poolUpReqDto = new RecruitingModify.PoolUpRequestDto();
        poolUpReqDto.setPoolUpDate(LocalDateTime.now());
        given(mockRecruitingRepository.findById(anyLong())).willReturn(Optional.of(recruiting));
        given(mockMemberRepository.findByEmail(anyString())).willReturn(Optional.of(mockMember));

        // when
        recruitingService.poolUpRecruiting(recruitingId, poolUpReqDto, email);
        // then
        verify(mockRecruitingRepository).findById(anyLong());
        verify(mockMemberRepository).findByEmail(anyString());
        assertThat(recruiting.getPoolUpCount()).isEqualTo(1);
    }
}