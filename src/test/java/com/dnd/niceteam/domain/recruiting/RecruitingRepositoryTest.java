package com.dnd.niceteam.domain.recruiting;

import com.dnd.niceteam.common.TestJpaConfig;
import com.dnd.niceteam.domain.account.Account;
import com.dnd.niceteam.domain.account.AccountRepository;
import com.dnd.niceteam.domain.code.*;
import com.dnd.niceteam.domain.department.Department;
import com.dnd.niceteam.domain.department.DepartmentRepository;
import com.dnd.niceteam.domain.member.Member;
import com.dnd.niceteam.domain.member.MemberRepository;
import com.dnd.niceteam.domain.memberscore.MemberScore;
import com.dnd.niceteam.domain.memberscore.MemberScoreRepository;
import com.dnd.niceteam.domain.project.*;
import com.dnd.niceteam.domain.university.University;
import com.dnd.niceteam.domain.university.UniversityRepository;
import org.junit.jupiter.api.BeforeEach;
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
import java.time.LocalTime;
import java.util.List;
import java.util.Set;

import static com.dnd.niceteam.comment.EntityFactoryForTest.*;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Transactional
@Import(TestJpaConfig.class)
class RecruitingRepositoryTest {
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
    private ProjectRepository projectRepository;
    @Autowired
    private RecruitingRepository recruitingRepository;

    @Autowired
    private EntityManager em;

    University university;
    Department department;
    MemberScore memberScore;
    Account account;
    Member member;
    Project project;
    Project project2;
    Recruiting recruiting;
    Recruiting recruiting2;
    int page = 1;
    int perSize = 5;
    Pageable pageable = PageRequest.of(page - 1, perSize);

    @BeforeEach
    void init() {
        university = universityRepository.save(createUniversity());
        department = departmentRepository.save(createDepartment(university));
        memberScore = memberScoreRepository.save(createMemberScore());
        account = accountRepository.save(createAccount());
        member = memberRepository.save(createMember(account, university, department, memberScore));
        em.flush();
    }

    @Test
    @DisplayName("내가 쓴글 - 전체 조회 Repository Test")
    void findMyRecruiting_ByMember() {
        // given
        project = projectRepository.save(createLectureProject(department));
        project2 = projectRepository.save(createLectureProject(department));
        recruiting = recruitingRepository.save(createRecruiting(member, project, Type.LECTURE));
        recruiting2 = recruitingRepository.save(createRecruiting(member, project2, Type.LECTURE));
        em.flush();
        em.clear();

        // when
        Page<Recruiting> foundMyRecruitings = recruitingRepository.findPageByMemberOrderByCreatedDateDesc(pageable, member);

        assertThat(foundMyRecruitings.getTotalPages()).isEqualTo(1);
        assertThat(foundMyRecruitings.getTotalElements()).isEqualTo(2);
        assertThat(foundMyRecruitings.getContent().size()).isEqualTo(2);
        assertThat(foundMyRecruitings.getContent().get(0).getTitle()).isEqualTo("test-title");
        assertThat(foundMyRecruitings.getContent().get(0).getProject().getName()).isEqualTo("project-name");
    }

    // TODO: 2022-08-20 내가 쓴글이 아닌 경우 목록 조회 테스트 추가 필요
    @Test
    @DisplayName("내가 쓴글 - 모집종료 상태 모집글 필터링 조회 Repository Test")
    void findMyRecruiting_ByMemberIdAndStatus() {
        // given
        project = projectRepository.save(createLectureProject(department));
        project2 = projectRepository.save(createLectureProject(department));
        Recruiting doneRecruiting = createRecruiting(member, project, Type.LECTURE);
        doneRecruiting.updateStatus(RecruitingStatus.DONE);
        recruiting = createRecruiting(member, project2, Type.LECTURE);
        recruitingRepository.save(doneRecruiting);
        recruitingRepository.save(recruiting);

        em.flush();
        em.clear();

        // when
        Page<Recruiting> foundMyRecruitings = recruitingRepository.findPageByMemberAndStatusOrderByCreatedDateDesc(pageable, member, RecruitingStatus.DONE);

        assertThat(foundMyRecruitings.getTotalPages()).isEqualTo(1);
        assertThat(foundMyRecruitings.getTotalElements()).isEqualTo(1);
        assertThat(foundMyRecruitings.getContent().get(0).getStatus()).isEqualTo(doneRecruiting.getStatus());
        assertThat(foundMyRecruitings.getContent().size()).isEqualTo(1);
        assertThat(foundMyRecruitings.getContent().get(0).getTitle()).isEqualTo("test-title");
    }

    @Test
    @DisplayName("홈 - 사이드 모집글 추천 조회 Repository Test")
    void find_RecommendedRecruiting() {
        // given
        Account account2 = accountRepository.save(Account.builder()
                .email("second-account")
                .password("secondtestPassword123!@#")
                .build());
        MemberScore levelUpMemberScore = memberScoreRepository.save(MemberScore.builder()
                .level(2)
                .reviewNum(10)
                .totalParticipationScore(100)
                .totalTeamAgainScore(100)
                .build());
        Member levelUpMember = memberRepository.save(Member.builder()
                .account(account2)
                .university(university)
                .department(department)
                .memberScore(levelUpMemberScore)
                .nickname("나는작성자")
                .admissionYear(2017)
                .personality(new Personality(Personality.Adjective.LOGICAL, Personality.Noun.LEADER))
                .interestingFields(Set.of(Field.IT_SW_GAME))
                .introduction("")
                .introductionUrl("")
                .build());

        Project lectureProject = projectRepository.save(createLectureProject(department));
        recruitingRepository.save(createRecruiting(member, lectureProject, Type.LECTURE));

        Project unrecommendedProject = projectRepository.save(createSideProject());  // 관심없는 글
        recruitingRepository.save(createRecruiting(member, unrecommendedProject, Type.SIDE));

        Project recommendedSideProject1 = projectRepository.save(SideProject.builder()
                .name("recommended-side-project1-name")
                .field(Field.IT_SW_GAME)
                .fieldCategory(FieldCategory.CLUB)
                .startDate(LocalDate.of(2022, 7, 4))
                .endDate(LocalDate.of(2022, 8, 28))
                .build());
        Project recommendedSideProject2 = projectRepository.save(SideProject.builder()
                .name("recommended-side-project2-name")
                .field(Field.DESIGN)
                .fieldCategory(FieldCategory.CLUB)
                .startDate(LocalDate.of(2022, 7, 4))
                .endDate(LocalDate.of(2022, 8, 28))
                .build());
        recruiting = recruitingRepository.save(createRecruiting(levelUpMember, recommendedSideProject1, Type.SIDE));
        recruiting2 = recruitingRepository.save(createRecruiting(member, recommendedSideProject2, Type.SIDE));
        em.flush();
        em.clear();

        // when
        pageable = PageRequest.of(page - 1, 4);
        Page<Recruiting> foundRecommendedRecruitings = recruitingRepository.findAllByInterestingFieldsOrderByWriterLevel(member.getInterestingFields(), pageable);

        // then
        assertThat(foundRecommendedRecruitings.getTotalElements()).isEqualTo(2);
        assertThat(foundRecommendedRecruitings.getTotalPages()).isEqualTo(1);
        assertThat(foundRecommendedRecruitings.getContent().get(0).getMember().getMemberScore().getLevel()).isEqualTo(2);
        assertThat(foundRecommendedRecruitings.getContent().get(1).getMember().getMemberScore().getLevel()).isEqualTo(1);
        assertThat(foundRecommendedRecruitings.getContent().get(0).getProject().getType()).isEqualTo(Type.SIDE);
        assertThat(foundRecommendedRecruitings.getContent().get(0).getProject().getName()).isEqualTo("recommended-side-project1-name");
    }

    @Test
    @DisplayName("사이드 모집글 검색 모든 경우의 수 테스트")
    void test_sideRecruiting_Search_Case() {
        Project sideProject0 = projectRepository.save(SideProject.builder()
                .name("project-0")
                .field(Field.IT_SW_GAME)
                .fieldCategory(FieldCategory.CLUB)
                .startDate(LocalDate.of(2022, 7, 4))
                .endDate(LocalDate.of(2022, 8, 28))
                .build());
        Project sideProject1 = projectRepository.save(SideProject.builder()
                .name("project-")
                .field(Field.DESIGN)
                .fieldCategory(FieldCategory.CLUB)
                .startDate(LocalDate.of(2022, 7, 4))
                .endDate(LocalDate.of(2022, 8, 28))
                .build());
        Project sideProject2 = projectRepository.save(SideProject.builder()
                .name("project-side2")
                .field(Field.IT_SW_GAME)
                .fieldCategory(FieldCategory.CLUB)
                .startDate(LocalDate.of(2022, 7, 4))
                .endDate(LocalDate.of(2022, 8, 28))
                .build());
        recruitingRepository.save(Recruiting.builder()
                .member(member)
                .project(sideProject0)
                .title("project-name")
                .content("test-content")
                .recruitingMemberCount(4)
                .recruitingType(Type.SIDE)
                .activityDayTimes(createActivityDayTime())
                .activityArea(ActivityArea.ONLINE)
                .status(RecruitingStatus.IN_PROGRESS)
                .personalityAdjectives(Set.of(Personality.Adjective.LOGICAL, Personality.Adjective.GOAL_ORIENTED))
                .personalityNouns(Set.of(Personality.Noun.PERFECTIONIST, Personality.Noun.INVENTOR))
                .commentCount(0)
                .bookmarkCount(0)
                .poolUpCount(0)
                .introLink("test-introLink")
                .build());
        recruitingRepository.save(createRecruiting(member, sideProject1, Type.SIDE));
        recruitingRepository.save(createRecruiting(member, sideProject2, Type.SIDE));
        em.flush();
        em.clear();

        // when - 사이드
        List<Recruiting> sideRecruitingsWithKeyword = recruitingRepository.findAllSideBySearchWordAndFieldOrderByCreatedDate("proj", null, pageable).getContent();
        List<Recruiting> sideRecruitingsWithKeywordAndField = recruitingRepository.findAllSideBySearchWordAndFieldOrderByCreatedDate("proj", Field.IT_SW_GAME, pageable).getContent();
        List<Recruiting> sideRecruitingsWithField = recruitingRepository.findAllSideBySearchWordAndFieldOrderByCreatedDate(null, Field.IT_SW_GAME, pageable).getContent();
        List<Recruiting> sideRecruitings = recruitingRepository.findAllSideBySearchWordAndFieldOrderByCreatedDate(null, null, pageable).getContent();
        // then
        assertThat(sideRecruitingsWithKeyword.size()).isEqualTo(3);
        assertThat(sideRecruitingsWithKeywordAndField.size()).isEqualTo(2);
        assertThat(sideRecruitingsWithField.size()).isEqualTo(2);
        assertThat(sideRecruitings.get(0).getProject().getName()).isEqualTo("project-side2");   // just 최신순
    }

    @Test
    @DisplayName("강의 모집글 검색 모든 경우의 수 테스트")
    void test_lectureRecruiting_Search_Case() {
        Project lectureProjectNonsameDepartment = projectRepository.save(LectureProject.builder()
                .name("project-0")
                .professor("common-professor")
                .lectureTimes(Set.of(LectureTime.builder().dayOfWeek(DayOfWeek.MON).startTime(LocalTime.of(9,0)).build()))
                .department(departmentRepository.save(Department.builder()
                        .name("미디어커뮤니케이션 학과")
                        .mainBranchType("main")
                        .region("서울")
                        .collegeName("학교")
                        .university(university)
                        .build()))
                .startDate(LocalDate.of(2022, 7, 4))
                .endDate(LocalDate.of(2022, 8, 28))
                .build());
        Project lectureProject1 = projectRepository.save(LectureProject.builder()
                .name("project-")
                .professor("common-professor")
                .lectureTimes(Set.of(LectureTime.builder().dayOfWeek(DayOfWeek.MON).startTime(LocalTime.of(9,0)).build()))
                .department(department)
                .startDate(LocalDate.of(2022, 7, 4))
                .endDate(LocalDate.of(2022, 8, 28))
                .build());
        Project lectureProject2 = projectRepository.save(LectureProject.builder()
                .name("project-side2")
                .professor("only-professor")
                .lectureTimes(Set.of(LectureTime.builder().dayOfWeek(DayOfWeek.MON).startTime(LocalTime.of(9,0)).build()))
                .department(department)
                .startDate(LocalDate.of(2022, 7, 4))
                .endDate(LocalDate.of(2022, 8, 28))
                .build());
        recruitingRepository.save(Recruiting.builder()
                .member(member)
                .project(lectureProjectNonsameDepartment)
                .title("lecture-type-recruiting-title")
                .content("test-content")
                .recruitingMemberCount(4)
                .recruitingType(Type.LECTURE)
                .activityDayTimes(createActivityDayTime())
                .activityArea(ActivityArea.ONLINE)
                .status(RecruitingStatus.IN_PROGRESS)
                .personalityAdjectives(Set.of(Personality.Adjective.LOGICAL, Personality.Adjective.GOAL_ORIENTED))
                .personalityNouns(Set.of(Personality.Noun.PERFECTIONIST, Personality.Noun.INVENTOR))
                .commentCount(0)
                .bookmarkCount(0)
                .poolUpCount(0)
                .introLink("test-introLink")
                .build());
        recruitingRepository.save(createRecruiting(member, lectureProject1, Type.LECTURE));
        recruitingRepository.save(createRecruiting(member, lectureProject2, Type.LECTURE));
        em.flush();
        em.clear();

        // when - 강의
        List<Recruiting> lectureRecruitingsWithoutKeyword = recruitingRepository.findAllLectureBySearchWordAndDepartmentOrderByCreatedDate(null, department.getName(), pageable).getContent();  // 1개나와야함 (name은 같지만, sw가 다르므로
        List<Recruiting> lectureRecruitingsWithKeyword = recruitingRepository.findAllLectureBySearchWordAndDepartmentOrderByCreatedDate("project", department.getName(), pageable).getContent();  // 1개나와야함 (name은 같지만, sw가 다르므로
        List<Recruiting> lectureRecruitingsWithDiffKeyword = recruitingRepository.findAllLectureBySearchWordAndDepartmentOrderByCreatedDate("33", department.getName(), pageable).getContent();  // 1개나와야함 (name은 같지만, sw가 다르므로
        List<Recruiting> lectureRecruitingsWithKeywordSameWithLectureTitle = recruitingRepository.findAllLectureBySearchWordAndDepartmentOrderByCreatedDate("lecture-type-recruiting-title", department.getName(), pageable).getContent();  // 1개나와야함 (name은 같지만, sw가 다르므로
        List<Recruiting> lectureRecruitingsWithKeywordSameWithProfessor = recruitingRepository.findAllLectureBySearchWordAndDepartmentOrderByCreatedDate("only-professor", department.getName(), pageable).getContent();  // 1개나와야함 (name은 같지만, sw가 다르므로
        // then
        assertThat(lectureRecruitingsWithoutKeyword.size()).isEqualTo(2);   // 검색 없을 때는 같은 전공
        assertThat(lectureRecruitingsWithKeyword.size()).isEqualTo(3);   // 검색 있을 때는 전공 필터링X
        assertThat(lectureRecruitingsWithDiffKeyword.size()).isEqualTo(0);   // 검색어 중 일치하지 않으면 검색x
        assertThat(lectureRecruitingsWithKeywordSameWithLectureTitle.size()).isEqualTo(1);   // 검색어가 모집글 제목과 일치한 경우
        assertThat(lectureRecruitingsWithKeywordSameWithProfessor.size()).isEqualTo(1);   // 검색어가 교수명과 일치한 경우
        assertThat(lectureRecruitingsWithKeywordSameWithProfessor.get(0).getProject().getName()).isEqualTo("project-side2");
    }
}