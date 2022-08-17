package com.dnd.niceteam.applicant.service;

import com.dnd.niceteam.applicant.dto.ApplicantCreation;
import static com.dnd.niceteam.comment.EntityFactoryForTest.*;
import com.dnd.niceteam.common.TestJpaConfig;
import com.dnd.niceteam.domain.account.Account;
import com.dnd.niceteam.domain.account.AccountRepository;
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

    @BeforeEach
    void init() {
        //given
        university = universityRepository.save(createUniversity());
        department = departmentRepository.save(createDepartment(university));
        memberScore = memberScoreRepository.save(createMemberScore());
        account = accountRepository.save(createAccount());
        member = memberRepository.save(createMember(account, university, department, memberScore));
        project = projectRepository.save(createLectureProject(department));
        recruiting = recruitingRepository.save(createRecruiting(member, project));

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