package com.dnd.niceteam.domain.project;

import com.dnd.niceteam.common.TestJpaConfig;
import com.dnd.niceteam.domain.account.Account;
import com.dnd.niceteam.domain.account.AccountRepository;
import com.dnd.niceteam.domain.code.Personality;
import com.dnd.niceteam.domain.department.Department;
import com.dnd.niceteam.domain.department.DepartmentRepository;
import com.dnd.niceteam.domain.member.Member;
import com.dnd.niceteam.domain.member.MemberRepository;
import com.dnd.niceteam.domain.memberscore.MemberScore;
import com.dnd.niceteam.domain.memberscore.MemberScoreRepository;
import com.dnd.niceteam.domain.university.University;
import com.dnd.niceteam.domain.university.UniversityRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Transactional
@Import(TestJpaConfig.class)
class ProjectMemberRepositoryTest {

    @Autowired
    private LectureProjectRepository lectureProjectRepository;

    @Autowired
    private ProjectMemberRepository projectMemberRepository;

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
    private EntityManager em;

    @Test
    void findDoneProjectMemberByMember() {
        // given
        University university = universityRepository.save(University.builder()
                .name("테스트대학교")
                .emailDomain("email.com")
                .build());
        Department department = departmentRepository.save(Department.builder()
                .university(university)
                .collegeName("테스트단과대학")
                .name("테스트학과")
                .region("서울")
                .mainBranchType("본교")
                .build());
        MemberScore memberScore = memberScoreRepository.save(MemberScore.builder()
                .level(1)
                .reviewNum(0)
                .participationSum(0)
                .rematchingSum(0)
                .build());
        Account account = accountRepository.save(Account.builder()
                .email("test@email.com")
                .password("test-password")
                .build());
        Member member = memberRepository.save(Member.builder()
                .account(account)
                .university(university)
                .department(department)
                .memberScore(memberScore)
                .nickname("test-nickname")
                .admissionYear(2017)
                .personality(new Personality(Personality.Adjective.LOGICAL, Personality.Noun.LEADER))
                .introduction("")
                .introductionUrl("")
                .build());
        LectureProject doneProject = lectureProjectRepository.save(LectureProject.builder()
                .name("테스트 프로젝트 1")
                .startDate(LocalDate.of(2022, 3, 2))
                .endDate(LocalDate.of(2022, 6, 30))
                .department(department)
                .professor("교수1")
                .lectureTimes(Set.of(LectureTime.builder()
                        .day('월')
                        .startTime(LocalTime.of(1, 30))
                        .build()))
                .build());
        doneProject.end();
        projectMemberRepository.save(ProjectMember.builder()
                .project(doneProject)
                .member(member)
                .build());
        LectureProject notStartedProject = lectureProjectRepository.save(LectureProject.builder()
                .name("테스트 프로젝트 2")
                .startDate(LocalDate.of(2022, 3, 2))
                .endDate(LocalDate.of(2022, 6, 30))
                .department(department)
                .professor("교수1")
                .lectureTimes(Set.of(LectureTime.builder()
                        .day('월')
                        .startTime(LocalTime.of(1, 30))
                        .build()))
                .build());
        projectMemberRepository.save(ProjectMember.builder()
                .project(notStartedProject)
                .member(member)
                .build());
        em.flush();
        em.clear();

        // when
        List<ProjectMember> projectMembers = projectMemberRepository.findDoneProjectMemberByMember(member);

        // then
        assertThat(projectMembers).hasSize(1);
        assertThat(projectMembers.stream().map(ProjectMember::getProject).collect(Collectors.toList()))
                .extracting("status").containsOnly(ProjectStatus.DONE);
    }
}