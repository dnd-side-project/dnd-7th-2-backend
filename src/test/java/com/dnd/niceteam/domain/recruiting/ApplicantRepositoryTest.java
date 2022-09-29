package com.dnd.niceteam.domain.recruiting;

import static com.dnd.niceteam.comment.EntityFactoryForTest.*;

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
import com.dnd.niceteam.domain.recruiting.exception.ApplicantNotFoundException;
import com.dnd.niceteam.domain.recruiting.exception.RecruitingNotFoundException;
import com.dnd.niceteam.domain.university.University;
import com.dnd.niceteam.domain.university.UniversityRepository;
import com.dnd.niceteam.error.exception.ErrorCode;
import org.junit.jupiter.api.Assertions;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Transactional
@Import(TestJpaConfig.class)
class ApplicantRepositoryTest {
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    UniversityRepository universityRepository;
    @Autowired
    DepartmentRepository departmentRepository;
    @Autowired
    MemberScoreRepository memberScoreRepository;
    @Autowired
    RecruitingRepository recruitingRepository;
    @Autowired
    ProjectRepository projectRepisotory;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    ApplicantRepository applicantRepository;

    Account account;
    University univ;
    Department department;
    MemberScore memberScore;
    Member member;
    Project project;
    Recruiting recruiting;
    Applicant applicant1;

    @BeforeEach
    void init() {
        account = accountRepository.save(createAccount());
        univ = universityRepository.save(createUniversity());
        department = departmentRepository.save(createDepartment(univ));
        memberScore = memberScoreRepository.save(createMemberScore());
        member = memberRepository.save(createMember(account, univ, department, memberScore));
        project = projectRepisotory.save(createLectureProject(department));
        recruiting = recruitingRepository.save(createRecruiting(member, project, Type.LECTURE));
        applicant1 = applicantRepository.save(Applicant.builder()
                .member(member)
                .recruiting(recruiting)
                .joined(Boolean.TRUE)
                .build());
    }

    @Test
    @DisplayName("모집글 지원 현황 목록")
    void myApplicantList () {
        // given
        MemberScore memberScore2 = memberScoreRepository.save(MemberScore.builder()
                .level(1)
                .reviewNum(0)
                .totalParticipationScore(0)
                .totalTeamAgainScore(0)
                .build());
        Account account2 = accountRepository.save(Account.builder()
                .email("another-email@naver.com")
                .password("test-password2")
                .build());
        Member anotherMember = memberRepository.save(Member.builder()
                .account(account2)
                .university(univ)
                .department(department)
                .memberScore(memberScore2)
                .nickname("테스트닉네임2")
                .admissionYear(2017)
                .personality(new Personality(Personality.Adjective.LOGICAL, Personality.Noun.LEADER))
                .interestingFields(Set.of(Field.IT_SW_GAME, Field.DESIGN))
                .introduction("")
                .introductionUrl("")
                .build());
        Applicant applicant2 = applicantRepository.save(Applicant.builder()
                .member(anotherMember)
                .recruiting(recruiting)
                .joined(Boolean.FALSE)
                .build());
        Project project1 = projectRepisotory.save(SideProject.builder()
                .name("project1 name")
                .startDate(LocalDate.of(2022,12,20))
                .endDate(LocalDate.of(2023,3,20))
                .field(Field.IT_SW_GAME)
                .fieldCategory(FieldCategory.STUDY)
                .build());
        Recruiting recruiting1 = recruitingRepository.save(Recruiting.builder()
                .content("another recruiting content")
                .title("another recruiting title")
                .member(anotherMember)
                .project(project1)
                .introLink("another recruiting introLink")
                .activityArea(ActivityArea.BUSAN)
                .recruitingMemberCount(4)
                .recruitingType(Type.SIDE)
                .recruitingEndDate(LocalDate.of(2022, 12, 29))
                .status(RecruitingStatus.FAILED)
                .commentCount(0)
                .poolUpCount(0)
                .poolUpDate(LocalDateTime.now())
                .bookmarkCount(0)
                .personalityNouns(Set.of(Personality.Noun.JACK_OF_ALL_TRADES))
                .personalityAdjectives(Set.of(Personality.Adjective.LOGICAL))
                .activityDayTimes(Set.of(ActivityDayTime.builder().
                        dayOfWeek(DayOfWeek.FRI)
                        .startTime(LocalTime.of(17, 0)).endTime(LocalTime.of(20, 0))
                        .build()))
                .build());
        applicantRepository.save(Applicant.builder()
                        .member(member)
                        .recruiting(recruiting1)
                        .joined(Boolean.FALSE)
                        .build());
        Pageable pageable = PageRequest.of(0, 10);

        // when
        Page<Applicant> myApplicantsPage = applicantRepository.findAllByMemberAndRecruitingStatusAndJoinedOrderByCreatedDateDesc(member, null, null, pageable);
        Page<Applicant> myApplicantsPageWithFiltering = applicantRepository.findAllByMemberAndRecruitingStatusAndJoinedOrderByCreatedDateDesc(anotherMember, RecruitingStatus.IN_PROGRESS, Boolean.TRUE, pageable);
        Page<Applicant> myApplicantsPageWithFilteredPage = applicantRepository.findAllByMemberAndRecruitingStatusAndJoinedOrderByCreatedDateDesc(member, RecruitingStatus.FAILED, Boolean.FALSE, pageable);

        // then
        assertThat(myApplicantsPage.getContent().size()).isEqualTo(2);  // 전체조회
        assertThat(myApplicantsPageWithFiltering.getContent().size()).isZero();
        assertThat(myApplicantsPageWithFilteredPage.getContent().size()).isEqualTo(1);
    }

    @Test
    @DisplayName("지원 취소")
    void removeApplicant() {
        // given
        Recruiting recruiting2 = recruitingRepository.save(Recruiting.builder()
                .content("another recruiting content")
                .title("another recruiting title")
                .member(member)
                .project(project)
                .introLink("another recruiting introLink")
                .activityArea(ActivityArea.BUSAN)
                .recruitingMemberCount(4)
                .recruitingType(Type.SIDE)
                .recruitingEndDate(LocalDate.of(2022, 12, 29))
                .status(RecruitingStatus.FAILED)
                .commentCount(0)
                .poolUpCount(0)
                .poolUpDate(LocalDateTime.now())
                .bookmarkCount(0)
                .personalityNouns(Set.of(Personality.Noun.JACK_OF_ALL_TRADES))
                .personalityAdjectives(Set.of(Personality.Adjective.LOGICAL))
                .activityDayTimes(Set.of(ActivityDayTime.builder().
                        dayOfWeek(DayOfWeek.FRI)
                        .startTime(LocalTime.of(17, 0)).endTime(LocalTime.of(20, 0))
                        .build()))
                .build());
        Applicant applicant = applicantRepository.save(Applicant.builder()
                .member(member)
                .recruiting(recruiting2)
                .joined(Boolean.FALSE)
                .build());
        applicantRepository.delete(applicant);
        // when
        Recruiting foundRecruiting = recruitingRepository.findById(recruiting2.getId())
                .orElseThrow(() -> new RecruitingNotFoundException("recruiting not found"));
        // then
        assertThat(foundRecruiting.getApplicants().size()).isZero();
    }
    @Test
    @DisplayName("모집글 제거 시 지원자 제거")
    void removeApplicant_afterRemoveRecruiting() {
        // given
        Applicant applicant = applicantRepository.save(Applicant.builder()
                .member(member)
                .recruiting(recruiting)
                .joined(Boolean.FALSE)
                .build());
        recruiting.addApplicant(applicant);
        recruitingRepository.delete(recruiting);
        // when
        RuntimeException e = Assertions.assertThrows(ApplicantNotFoundException.class
                , () -> applicantRepository.findById(applicant.getId())
                        .orElseThrow(() -> new ApplicantNotFoundException("존재하지 않는 지원자")));
        // then
        assertThat(e.getMessage()).startsWith(ErrorCode.APPLICANT_NOT_FOUND.name());
    }
}