package com.dnd.niceteam.recruiting.service;

import com.dnd.niceteam.comment.DtoFactoryForTest;
import com.dnd.niceteam.common.TestJpaConfig;
import com.dnd.niceteam.common.dto.Pagination;
import com.dnd.niceteam.domain.account.Account;
import com.dnd.niceteam.domain.account.AccountRepository;
import com.dnd.niceteam.domain.code.*;
import com.dnd.niceteam.domain.department.Department;
import com.dnd.niceteam.domain.department.DepartmentRepository;
import com.dnd.niceteam.domain.member.Member;
import com.dnd.niceteam.domain.member.MemberRepository;
import com.dnd.niceteam.domain.memberscore.MemberScore;
import com.dnd.niceteam.domain.memberscore.MemberScoreRepository;
import com.dnd.niceteam.domain.project.LectureProject;
import com.dnd.niceteam.domain.project.Project;
import com.dnd.niceteam.domain.project.ProjectRepository;
import com.dnd.niceteam.domain.project.SideProject;
import com.dnd.niceteam.domain.recruiting.Recruiting;
import com.dnd.niceteam.domain.recruiting.RecruitingRepository;
import com.dnd.niceteam.domain.recruiting.RecruitingStatus;
import com.dnd.niceteam.domain.recruiting.exception.RecruitingNotFoundException;
import com.dnd.niceteam.domain.university.University;
import com.dnd.niceteam.domain.university.UniversityRepository;
import com.dnd.niceteam.project.exception.ProjectNotFoundException;
import com.dnd.niceteam.project.service.ProjectMemberService;
import com.dnd.niceteam.project.service.ProjectService;
import com.dnd.niceteam.recruiting.dto.RecruitingCreation;
import com.dnd.niceteam.recruiting.dto.RecruitingFind;
import com.dnd.niceteam.recruiting.dto.RecruitingModify;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.Set;

import static com.dnd.niceteam.comment.EntityFactoryForTest.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
@Import({TestJpaConfig.class, RecruitingService.class, ProjectService.class, ProjectMemberService.class})
@Transactional
class RecruitingServiceTest {
    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private UniversityRepository universityRepository;

    @Autowired
    private DepartmentRepository departmentRepository;
    @Autowired
    private MemberScoreRepository memberScoreRepository;

    @Autowired
    private RecruitingRepository recruitingRepository;
    @Autowired
    private RecruitingService recruitingService;

    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private ProjectService projectService;

    @Autowired
    private EntityManager em;

    University university;
    Department department;
    MemberScore memberScore;
    Account account;
    Member member;
    private final int page = 1;
    private final int perSize = 5;
    Pageable pageable = PageRequest.of(page - 1, perSize);

    @BeforeEach
    void init() {
        university = universityRepository.save(createUniversity());
        department = departmentRepository.save(createDepartment(university));
        memberScore = memberScoreRepository.save(createMemberScore());
        account = accountRepository.save(createAccount());
        member = memberRepository.save(createMember(account, university, department, memberScore));

        em.flush();
        em.clear();
    }

    @Disabled
    @DisplayName("(Lecture)신규 모집글 작성 서비스 테스트 코드 ")
    @Test
    public void postRecruiting() {
        // given
        RecruitingCreation.RequestDto recruitingReqDto = DtoFactoryForTest.createLectureRecruitingRequest();
        //when
        RecruitingCreation.ResponseDto recruitingResDto = recruitingService.addProjectAndRecruiting(account.getEmail(), recruitingReqDto);

        //then
        Recruiting createdRecruiting = recruitingRepository.findById(recruitingResDto.getRecruitingId())
                        .orElseThrow(() -> new RecruitingNotFoundException("recruitingId = " + recruitingResDto.getRecruitingId()));
        Project createdProject = projectRepository.findById(createdRecruiting.getProject().getId())
                .orElseThrow(() -> new ProjectNotFoundException(createdRecruiting.getProject().getId()));

        assertThat(createdRecruiting.getPersonalityAdjectives()).size().isEqualTo(2);
        assertThat(createdRecruiting.getId()).isEqualTo(recruitingResDto.getRecruitingId());

        assertThat(createdProject.getId()).isEqualTo(recruitingResDto.getProjectId());
    }

    @DisplayName("(Lecture) 모집글 상세 조회 서비스 테스트 코드")
    @Test
    public void detailRecruiting() {
        // given
        Project savedProject = projectRepository.save(createLectureProject(department));
        Recruiting savedRecruiting = recruitingRepository.save(createRecruiting(member, savedProject, Type.LECTURE));

        //when
        RecruitingFind.DetailResponseDto responseDto = recruitingService.getRecruiting(savedRecruiting.getId(), account.getEmail());

        //then
        Recruiting foundRecruiting = recruitingRepository.findById(savedRecruiting.getId())
                .orElseThrow(() -> new RecruitingNotFoundException("recruitingId = " + savedRecruiting.getId()));
        Project foundProject = projectRepository.findById(responseDto.getProjectResponse().getId())
                .orElseThrow(() -> new ProjectNotFoundException(responseDto.getProjectResponse().getId()));

        assertThat(foundRecruiting.getPersonalityNouns()).size().isEqualTo(2);
        assertThat(foundRecruiting.getTitle()).isEqualTo(responseDto.getTitle());
        assertThat(foundRecruiting.getProject().getType()).isEqualTo(foundProject.getType());
    }

    @DisplayName("내가 쓴 글 전체 조회 및 상태 별 조회 서비스 테스트 코드")
    @Test
    public void getMyRecruitingsPage() {
        // given
        Project savedLectureProject = projectRepository.save(createLectureProject(department));
        Project savedSideProject = projectRepository.save(createSideProject());
        Recruiting savedRecruitingWithDone = recruitingRepository.save(createRecruiting(member, savedLectureProject, Type.LECTURE));
        savedRecruitingWithDone.updateStatus(RecruitingStatus.DONE);
        Recruiting savedRecruiting2 = recruitingRepository.save(createRecruiting(member, savedSideProject, Type.SIDE));

        //when - 전체 조회
        Pagination<RecruitingFind.ListResponseDto> myRecruitings = recruitingService.getMyRecruitings(page, perSize, null, account.getEmail());

        // then
        Page<Recruiting> foundMyRecruitingsPage = recruitingRepository.findPageByMemberOrderByCreatedDateDesc(pageable, member);

        assertThat(myRecruitings.getTotalCount()).isEqualTo(foundMyRecruitingsPage.getTotalElements());
        assertThat(myRecruitings.getTotalPages()).isEqualTo(1);
        assertThat(myRecruitings.getPerSize()).isEqualTo(perSize);
        assertThat(myRecruitings.getContents().get(0).getType()).isEqualTo(savedRecruiting2.getRecruitingType());
        assertThat(myRecruitings.getContents().get(1).getProfessor()).isEqualTo("test-professor");
    }

    @DisplayName("사이드 모집글 추천 서비스 테스트 코드")
    @Test
    public void getRecommendedSideRecruitingPage() {
        // given
        Project savedLectureProject = projectRepository.save(createLectureProject(department));
        Project savedSideProject = projectRepository.save(createSideProject());
        Project savedRecommendedSideProject = projectRepository.save(SideProject.builder()
                        .name("recommended-project-name")
                        .field(Field.DESIGN)
                        .fieldCategory(FieldCategory.STUDY)
                        .startDate(LocalDate.of(2022, 7, 4))
                        .endDate(LocalDate.of(2022, 8, 30))
                        .build()
        );
        recruitingRepository.save(createRecruiting(member, savedLectureProject, Type.LECTURE));
        recruitingRepository.save(createRecruiting(member, savedSideProject, Type.SIDE));
        Recruiting savedRecommendedRecruiting = recruitingRepository.save(createRecruiting(member, savedRecommendedSideProject, Type.SIDE));

        //when
        Pagination<RecruitingFind.RecommendedListResponseDto> recommendedRecruitings = recruitingService.getRecommendedRecruitings(page, perSize, account.getEmail());

        // then
        Page<Recruiting> foundRecommendedRecruitingsPage = recruitingRepository.findAllByInterestingFieldsOrderByWriterLevel(member.getInterestingFields(), pageable);

        assertThat(recommendedRecruitings.getTotalCount()).isEqualTo(foundRecommendedRecruitingsPage.getTotalElements());
        assertThat(recommendedRecruitings.getPage()).isEqualTo(0);
        assertThat(recommendedRecruitings.getTotalPages()).isEqualTo(1);
        assertThat(recommendedRecruitings.getPerSize()).isEqualTo(5);
        assertThat(recommendedRecruitings.getTotalPages()).isEqualTo(foundRecommendedRecruitingsPage.getTotalPages());
        assertThat(recommendedRecruitings.getContents().get(0).getRecruitingId()).isEqualTo(savedRecommendedRecruiting.getId());
        assertThat(recommendedRecruitings.getContents().size()).isEqualTo(1);
    }

    @DisplayName("모집글 키워드 검색 서비스 테스트 코드")
    @Test
    public void getSearchRecruitingWithKeywordPage() {
        // given
        String projectName = "DND 동아리원 구해요!";
        Project savedLectureProject = projectRepository.save(createLectureProject(department));
        Project savedSideProject = projectRepository.save(SideProject.builder()
                .name(projectName)
                .field(Field.DESIGN)
                .fieldCategory(FieldCategory.STUDY)
                .startDate(LocalDate.of(2022, 7, 4))
                .endDate(LocalDate.of(2022, 8, 30))
                .build()
        );
        recruitingRepository.save(createRecruiting(member, savedLectureProject, Type.LECTURE));
        recruitingRepository.save(createRecruiting(member, savedSideProject, Type.SIDE));

        // when (SIDE탭에서 키워드 검색)
        Pagination<RecruitingFind.ListResponseDto> searchRecruitingPage = recruitingService.getSearchRecruitings(page, perSize, null, Type.SIDE, "DND", account.getEmail());
        Pagination<RecruitingFind.ListResponseDto> nonSearchRecruitingPage = recruitingService.getSearchRecruitings(page, perSize, null, Type.SIDE, "DND동아리원", account.getEmail());

        // then
        Page<Recruiting> foundRecommendedRecruitingsPage = recruitingRepository.findAllSideBySearchWordAndFieldOrderByCreatedDate("DND", null, pageable);
        // 검색 o
        assertThat(searchRecruitingPage.getTotalCount()).isEqualTo(foundRecommendedRecruitingsPage.getTotalElements());
        assertThat(searchRecruitingPage.getPage()).isEqualTo(0);
        assertThat(searchRecruitingPage.getTotalPages()).isEqualTo(1);
        assertThat(searchRecruitingPage.getContents().get(0).getProjectName()).isEqualTo(projectName);
        assertThat(searchRecruitingPage.getContents().size()).isEqualTo(1);
        // 검색 x
        assertThat(nonSearchRecruitingPage.getTotalCount()).isEqualTo(0);
        assertThat(nonSearchRecruitingPage.getContents().size()).isEqualTo(0);
    }


    @DisplayName("사이드 모집글 검색 목록 조회 서비스 테스트 코드")
    @Test
    public void getSearchRecruitingPage() {
        // given
        String projectName = "나중에 등록된 프로젝트!";
        Project lectureProjectWithSameDeaprtment = projectRepository.save(createLectureProject(department));
        Project lectureProjectWithNonsameDepartment = projectRepository.save(createLectureProject(departmentRepository.save(Department.builder()
                .university(university)
                .collegeName("학교")
                .region("부산")
                .mainBranchType("main")
                .name("다른 학과")
                .build())));

        Project savedSideProject = projectRepository.save(createSideProject());
        Project afterSavedSideProject = projectRepository.save(SideProject.builder()
                .name(projectName)
                .field(Field.DESIGN)
                .fieldCategory(FieldCategory.STUDY)
                .startDate(LocalDate.of(2022, 7, 4))
                .endDate(LocalDate.of(2022, 8, 30))
                .build()
        );
        recruitingRepository.save(createRecruiting(member, lectureProjectWithNonsameDepartment, Type.LECTURE));
        recruitingRepository.save(createRecruiting(member, lectureProjectWithSameDeaprtment, Type.LECTURE));
        recruitingRepository.save(createRecruiting(member, savedSideProject, Type.SIDE));
        recruitingRepository.save(createRecruiting(member, afterSavedSideProject, Type.SIDE));

        // when
        Pagination<RecruitingFind.ListResponseDto> allSideRecruitingsPage = recruitingService.getSearchRecruitings(page, perSize, null, Type.SIDE, null, account.getEmail());   // side - 전체 조회
        Pagination<RecruitingFind.ListResponseDto> allSideRecruitingsPageWithFiltered = recruitingService.getSearchRecruitings(page, perSize, Field.DESIGN, Type.SIDE, null, account.getEmail());   // side - 분야 필터링 조회

        // then
        assertThat(allSideRecruitingsPage.getContents().size()).isEqualTo(2);
        assertThat(allSideRecruitingsPage.getPage()).isZero();
        assertThat(allSideRecruitingsPage.getTotalPages()).isEqualTo(1);
        assertThat(allSideRecruitingsPage.getContents().get(0).getProjectName()).isEqualTo(projectName);

        assertThat(allSideRecruitingsPageWithFiltered.getContents().size()).isEqualTo(1);
        assertThat(allSideRecruitingsPageWithFiltered.getContents().get(0).getField()).isEqualTo(Field.DESIGN);
    }

    @DisplayName("강의 모집글 검색 목록 조회 서비스 테스트 코드")
    @Test
    public void getRecruitingPageWithFilter() {
        // given
        Project lectureProjectWithSameDeaprtment = projectRepository.save(createLectureProject(department));
        Project lectureProjectWithNonsameDepartment = projectRepository.save(createLectureProject(departmentRepository.save(Department.builder()
                .university(university)
                .collegeName("학교")
                .region("부산")
                .mainBranchType("main")
                .name("다른 학과")
                .build())));

        Project savedSideProject = projectRepository.save(createSideProject());
        Project afterSavedSideProject = projectRepository.save(createSideProject());
        recruitingRepository.save(createRecruiting(member, lectureProjectWithNonsameDepartment, Type.LECTURE));
        recruitingRepository.save(createRecruiting(member, lectureProjectWithSameDeaprtment, Type.LECTURE));
        recruitingRepository.save(createRecruiting(member, savedSideProject, Type.SIDE));
        recruitingRepository.save(createRecruiting(member, afterSavedSideProject, Type.SIDE));
        // when
        Pagination<RecruitingFind.ListResponseDto> allLectureRecruitingsPageSameWithDepartment = recruitingService.getSearchRecruitings(page, perSize, null, Type.LECTURE, null, account.getEmail()); // 전체 조회 (전공 학과 필터링)
        Pagination<RecruitingFind.ListResponseDto> allLectureRecruitingsPageSameWithKeyword = recruitingService.getSearchRecruitings(page, perSize, null, Type.LECTURE, "test-professor" , account.getEmail()); // 검색 (검색어 필터링)

        // then
        Page<Recruiting> foundLectureRecruitingPagSameWithDepartment = recruitingRepository.findAllLectureBySearchWordAndDepartmentOrderByCreatedDate(null, member.getDepartment().getName(), pageable);
        assertThat(allLectureRecruitingsPageSameWithDepartment.getContents().size()).isEqualTo(1);
        assertThat(allLectureRecruitingsPageSameWithDepartment.getPage()).isZero();
        LectureProject foundLectureProject = (LectureProject)foundLectureRecruitingPagSameWithDepartment.getContent().get(0).getProject();
        assertThat(foundLectureProject.getDepartment().getName()).isEqualTo(member.getDepartment().getName());

        assertThat(allLectureRecruitingsPageSameWithKeyword.getTotalCount()).isEqualTo(2);  // 교수명 검색 -> 전공 필터링X
    }

    // TODO: 2022-08-23 PROJECT_NOT_FOUND 해결 필요
    @Disabled
    @DisplayName("(LECTURE) 모집글 수정 서비스 테스트 코드")
    @Test
    public void modifyRecruiting() {
        // given
        Project savedLectureProject = projectRepository.save(createLectureProject(department));
        Recruiting savedRecruiting = recruitingRepository.save(createRecruiting(member, savedLectureProject, Type.LECTURE));
        RecruitingModify.RequestDto recruitingReqDto = DtoFactoryForTest.createRecruitingModifyRequest();
        recruitingReqDto.setActivityArea(ActivityArea.BUSAN);
        recruitingReqDto.setPersonalityNouns(Set.of(Personality.Noun.JACK_OF_ALL_TRADES));
        recruitingReqDto.setRecruitingMemberCount(savedRecruiting.getRecruitingMemberCount()+1);    // update

        //when
        RecruitingModify.ResponseDto recruitingResDto = recruitingService.modifyProjectAndRecruiting(savedRecruiting.getId(), recruitingReqDto);

        //then
        Recruiting updatedRecruiting = recruitingRepository.findById(savedRecruiting.getId())
                .orElseThrow(() -> new RecruitingNotFoundException("recruiting id : " + savedRecruiting.getId()));
        assertThat(savedRecruiting.getActivityArea()).isNotEqualTo(updatedRecruiting.getActivityArea());
        assertThat(savedRecruiting.getPersonalityNouns()).isNotEqualTo(updatedRecruiting.getPersonalityNouns());
        assertThat(savedRecruiting.getPersonalityAdjectives()).isEqualTo(updatedRecruiting.getPersonalityAdjectives());

        assertThat(recruitingResDto.getProjectId()).isEqualTo(updatedRecruiting.getProject().getId());
    }

    @DisplayName("모집글 제거 서비스 테스트 코드")
    @Test
    public void removeRecruiting() {
        // given
        Project saveSideProject = projectRepository.save(createSideProject());
        Recruiting savedRecruiting = recruitingRepository.save(createRecruiting(member, saveSideProject, Type.SIDE));
        // when
        recruitingService.removeRecruiting(savedRecruiting.getId());

        // expected
        assertThatThrownBy(() -> recruitingService.getRecruiting(savedRecruiting.getId(), account.getEmail()))
                .isInstanceOf(RecruitingNotFoundException.class);
    }
}