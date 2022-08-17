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
import com.dnd.niceteam.domain.project.Project;
import com.dnd.niceteam.domain.project.ProjectRepository;
import com.dnd.niceteam.domain.project.SideProject;
import com.dnd.niceteam.domain.university.University;
import com.dnd.niceteam.domain.university.UniversityRepository;
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
    void findMyRecruiting_ByMemberId() {
        // given
        project = projectRepository.save(createLectureProject(department));
        project2 = projectRepository.save(createLectureProject(department));
        recruiting = recruitingRepository.save(createRecruiting(member, project, Type.LECTURE));
        recruiting2 = recruitingRepository.save(createRecruiting(member, project2, Type.LECTURE));
        em.flush();
        em.clear();

        // when
        PageImpl<Recruiting> foundMyRecruitings = recruitingRepository.findAllByMemberId(1L, pageable);

        assertThat(foundMyRecruitings.getTotalPages()).isEqualTo(1);
        assertThat(foundMyRecruitings.getTotalElements()).isEqualTo(2);
        assertThat(foundMyRecruitings.getContent().size()).isEqualTo(2);
        assertThat(foundMyRecruitings.getContent().get(0).getTitle()).isEqualTo("test-title");
        assertThat(foundMyRecruitings.getContent().get(0).getProject().getName()).isEqualTo("project-name");
    }

    @Test
    @DisplayName("내가 쓴글 - 모집종료 상태 모집글 필터링 조회 Repository Test")
    void findMyRecruiting_ByMemberIdAndStatus() {
        // given
        project = projectRepository.save(createLectureProject(department));
        project2 = projectRepository.save(createLectureProject(department));
        recruiting = createRecruiting(member, project, Type.LECTURE);
        recruiting.updateStatus(ProgressStatus.DONE);
        recruitingRepository.save(recruiting);
        recruitingRepository.save(createRecruiting(member, project2, Type.LECTURE));

        em.flush();
        em.clear();

        // when
        PageImpl<Recruiting> foundMyRecruitings = recruitingRepository.findAllByMemberIdAndStatus(1L, ProgressStatus.DONE, pageable);

        assertThat(foundMyRecruitings.getTotalPages()).isEqualTo(1);
        assertThat(foundMyRecruitings.getTotalElements()).isEqualTo(1);
        assertThat(foundMyRecruitings.getContent().get(0).getStatus()).isEqualTo(recruiting.getStatus());
        assertThat(foundMyRecruitings.getContent().size()).isEqualTo(1);
        assertThat(foundMyRecruitings.getContent().get(0).getProject().getName()).isEqualTo("project-name");
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
        PageImpl<Recruiting> foundRecommendedRecruitings = recruitingRepository.findAllByInterestingFieldsOrderByWriterLevel(member.getInterestingFields(), pageable);

        // then
        assertThat(foundRecommendedRecruitings.getTotalElements()).isEqualTo(2);
        assertThat(foundRecommendedRecruitings.getTotalPages()).isEqualTo(1);
        assertThat(foundRecommendedRecruitings.getContent().get(0).getMember().getMemberScore().getLevel()).isEqualTo(2);
        assertThat(foundRecommendedRecruitings.getContent().get(1).getMember().getMemberScore().getLevel()).isEqualTo(1);
        assertThat(foundRecommendedRecruitings.getContent().get(0).getProject().getType()).isEqualTo(Type.SIDE);
        assertThat(foundRecommendedRecruitings.getContent().get(0).getProject().getName()).isEqualTo("recommended-side-project1-name");

    }
}