package com.dnd.niceteam.applicant.service;

import com.dnd.niceteam.applicant.dto.ApplicantCreation;
import com.dnd.niceteam.common.TestJpaConfig;
import com.dnd.niceteam.domain.account.Account;
import com.dnd.niceteam.domain.account.AccountRepository;
import com.dnd.niceteam.domain.code.ActivityArea;
import com.dnd.niceteam.domain.code.Personality;
import com.dnd.niceteam.domain.code.ProgressStatus;
import com.dnd.niceteam.domain.code.Type;
import com.dnd.niceteam.domain.department.Department;
import com.dnd.niceteam.domain.department.DepartmentRepository;
import com.dnd.niceteam.domain.member.Member;
import com.dnd.niceteam.domain.member.MemberRepository;
import com.dnd.niceteam.domain.memberscore.MemberScore;
import com.dnd.niceteam.domain.memberscore.MemberScoreRepository;
import com.dnd.niceteam.domain.project.*;
import com.dnd.niceteam.domain.recruiting.Applicant;
import com.dnd.niceteam.domain.recruiting.ApplicantRepository;
import com.dnd.niceteam.domain.recruiting.Recruiting;
import com.dnd.niceteam.domain.recruiting.RecruitingRepository;
import com.dnd.niceteam.domain.university.University;
import com.dnd.niceteam.domain.university.UniversityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@Import({TestJpaConfig.class, ApplicantService.class})
@Transactional
class ApplicantServiceTest {
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
    private LectureProjectRepository projectRepository;

    @Autowired
    private RecruitingRepository recruitingRepository;

    @Autowired
    private ApplicantRepository applicantRepository;

    @Autowired
    private ApplicantService applicantService;

    @Autowired
    private EntityManager em;

    University university;
    Department department;
    MemberScore memberScore;
    Account account;
    Member member;
    LectureProject project;
    Recruiting recruiting;

    // TODO: 2022-08-13 중복 제거 리팩토링 가능할지
    @BeforeEach
    void init() {
        //given
        university = universityRepository.save(University.builder()
                .name("테스트대학교")
                .emailDomain("email.com")
                .build());
        department = departmentRepository.save(Department.builder()
                .university(university)
                .collegeName("단과대학")
                .name("학과")
                .region("서울")
                .mainBranchType("본교")
                .build());
        memberScore = memberScoreRepository.save(MemberScore.builder()
                .level(1)
                .reviewNum(0)
                .participationSum(0)
                .rematchingSum(0)
                .build());
        account = accountRepository.save(Account.builder()
                .email("test@email.com")
                .password("testPassword123!@#")
                .build());
        member = memberRepository.save(Member.builder()
                .account(account)
                .university(university)
                .department(department)
                .memberScore(memberScore)
                .nickname("테스트닉네임")
                .admissionYear(2017)
                .personality(new Personality(Personality.Adjective.LOGICAL, Personality.Noun.LEADER))
                .introduction("")
                .introductionUrl("")
                .build());

        project = projectRepository.save(LectureProject.builder()
                .name("project-name")
                .department(department)
                .startDate(LocalDate.of(2022, 7, 4))
                .endDate(LocalDate.of(2022, 8, 28))
                .lectureTimes(Set.of(LectureTime.builder().day('월').startTime(LocalTime.of(9, 0)).build()))
                .professor("test-professor")
                .build()
        );

        recruiting = recruitingRepository.save(Recruiting.builder()
                .member(member)
                .project(project)
                .title("test-title")
                .content("test-content")
                .recruitingMemberCount(4)
                .recruitingType(Type.LECTURE)
                .activityArea(ActivityArea.ONLINE)
                .status(ProgressStatus.IN_PROGRESS)
                .commentCount(0)
                .bookmarkCount(0)
                .poolUpCount(0)
                .introLink("test-introLink")
                .build()
        );

        em.flush();
        em.clear();
    }

    @DisplayName("모집글에 지원합니다.")
    @Test
    void create() {
        // when
        ApplicantCreation.ResponseDto responseDto = applicantService.addApplicant(recruiting.getId(), member.getId());

        // then
        Applicant applicant = applicantRepository.findById(responseDto.getApplicantId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 지원자"));
        assertThat(applicant.getId()).isEqualTo(responseDto.getApplicantId());
        assertThat(applicant.getJoined()).isFalse();
        assertThat(applicant.getMember().getId()).isEqualTo(member.getId());
        assertThat(applicant.getRecruiting().getId()).isEqualTo(recruiting.getId());
    }

    /*@DisplayName("예외 - 모집글 지원 가능한 상태가 아닙니다.")
    @Test
    void ImpossibleApplyException() {
        recruiting.updateStatus(ProgressStatus.DONE);
        assertThatThrownBy(() -> applicantService.addApplicant(recruiting.getId(), member.getId()))
                .isInstanceOf(ApplyImpossibleRecruitingStatusException.class);
    }*/
}