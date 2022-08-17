package com.dnd.niceteam.recruiting.service;

import com.dnd.niceteam.comment.DtoFactoryForTest;
import com.dnd.niceteam.common.TestJpaConfig;
import com.dnd.niceteam.common.dto.Pagination;
import com.dnd.niceteam.domain.account.Account;
import com.dnd.niceteam.domain.account.AccountRepository;
import com.dnd.niceteam.domain.code.Field;
import com.dnd.niceteam.domain.code.FieldCategory;
import com.dnd.niceteam.domain.code.Type;
import com.dnd.niceteam.domain.department.Department;
import com.dnd.niceteam.domain.department.DepartmentRepository;
import com.dnd.niceteam.domain.member.Member;
import com.dnd.niceteam.domain.member.MemberRepository;
import com.dnd.niceteam.domain.memberscore.MemberScore;
import com.dnd.niceteam.domain.memberscore.MemberScoreRepository;
import com.dnd.niceteam.domain.project.*;
import com.dnd.niceteam.domain.recruiting.Recruiting;
import com.dnd.niceteam.domain.recruiting.RecruitingRepository;
import com.dnd.niceteam.domain.recruiting.exception.RecruitingNotFoundException;
import com.dnd.niceteam.domain.university.University;
import com.dnd.niceteam.domain.university.UniversityRepository;
import com.dnd.niceteam.project.exception.ProjectNotFoundException;
import com.dnd.niceteam.project.service.ProjectService;
import com.dnd.niceteam.recruiting.dto.RecruitingCreation;
import com.dnd.niceteam.recruiting.dto.RecruitingFind;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import java.time.LocalDate;

import static com.dnd.niceteam.comment.EntityFactoryForTest.*;
import static com.dnd.niceteam.comment.EntityFactoryForTest.createSideProject;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import({TestJpaConfig.class, RecruitingService.class, ProjectService.class})
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
                .orElseThrow(() -> new ProjectNotFoundException("projectId = " + createdRecruiting.getProject().getId()));

        // TODO: 2022-08-18 Personality 분리 후 수정 예정
        // assertThat(createdRecruiting.getPersonalities()).size().isEqualTo(4);
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
        RecruitingFind.DetailResponseDto responseDto = recruitingService.getRecruiting(savedRecruiting.getId());

        //then
        Recruiting foundRecruiting = recruitingRepository.findById(savedRecruiting.getId())
                .orElseThrow(() -> new RecruitingNotFoundException("recruitingId = " + savedRecruiting.getId()));
        Project createdProject = projectRepository.findById(responseDto.getProjectResponse().getId())
                .orElseThrow(() -> new ProjectNotFoundException("projectId = " + responseDto.getProjectResponse().getId()));

        // TODO: 2022-08-18 Personality 분리 후 수정 예정
        // assertThat(createdRecruiting.getPersonalities()).size().isEqualTo(4);
        assertThat(foundRecruiting.getTitle()).isEqualTo(responseDto.getTitle());
        assertThat(foundRecruiting.getProject().getType()).isEqualTo(createdProject.getType());
    }

    @DisplayName("내가 쓴 글 전체 조회 서비스 테스트 코드")
    @Test
    public void getMyRecruitingsPage() {
        // given
        Project savedLectureProject = projectRepository.save(createLectureProject(department));
        Project savedSideProject = projectRepository.save(createSideProject());
        Recruiting savedRecruiting1 = recruitingRepository.save(createRecruiting(member, savedLectureProject, Type.LECTURE));
        Recruiting savedRecruiting2 = recruitingRepository.save(createRecruiting(member, savedSideProject, Type.SIDE));

        //when
        Pagination<RecruitingFind.ListResponseDto> myRecruitings = recruitingService.getMyRecruitings(page, perSize, null, account.getEmail());

        // then
        PageImpl<Recruiting> foundMyRecruitingsPage = recruitingRepository.findAllByMemberId(member.getId(), pageable);

        assertThat(myRecruitings.getTotalCount()).isEqualTo(foundMyRecruitingsPage.getTotalElements());
        assertThat(myRecruitings.getPage()).isEqualTo(page);
        assertThat(myRecruitings.getPerSize()).isEqualTo(perSize);
        assertThat(myRecruitings.getTotalPages()).isEqualTo(foundMyRecruitingsPage.getTotalPages());
        assertThat(myRecruitings.getContents().get(0).getRecruitingType()).isEqualTo(savedRecruiting2.getRecruitingType());
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
        Recruiting savedRecruiting1 = recruitingRepository.save(createRecruiting(member, savedLectureProject, Type.LECTURE));
        Recruiting savedRecruiting2 = recruitingRepository.save(createRecruiting(member, savedSideProject, Type.SIDE));
        Recruiting savedRecommendedRecruiting = recruitingRepository.save(createRecruiting(member, savedRecommendedSideProject, Type.SIDE));

        //when
        Pagination<RecruitingFind.RecommendedListResponseDto> recommendedRecruitings = recruitingService.getRecommendedRecruiting(page, perSize, account.getEmail());

        // then
        PageImpl<Recruiting> foundRecommendedRecruitingsPage = recruitingRepository.findAllByInterestingFieldsOrderByWriterLevel(member.getInterestingFields(), pageable);

        assertThat(recommendedRecruitings.getTotalCount()).isEqualTo(foundRecommendedRecruitingsPage.getTotalElements());
        assertThat(recommendedRecruitings.getPage()).isEqualTo(page);
        assertThat(recommendedRecruitings.getPerSize()).isEqualTo(perSize);
        assertThat(recommendedRecruitings.getTotalPages()).isEqualTo(foundRecommendedRecruitingsPage.getTotalPages());
        assertThat(recommendedRecruitings.getContents().get(0).getRecruitingId()).isEqualTo(savedRecommendedRecruiting.getId());
        assertThat(recommendedRecruitings.getContents().size()).isEqualTo(1);
    }
}