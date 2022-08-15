package com.dnd.niceteam.comment.service;

import com.dnd.niceteam.comment.dto.CommentCreation;
import com.dnd.niceteam.common.TestJpaConfig;
import com.dnd.niceteam.domain.account.Account;
import com.dnd.niceteam.domain.account.AccountRepository;
import com.dnd.niceteam.domain.code.*;
import com.dnd.niceteam.domain.comment.Comment;
import com.dnd.niceteam.domain.comment.CommentRepository;
import com.dnd.niceteam.domain.department.Department;
import com.dnd.niceteam.domain.department.DepartmentRepository;
import com.dnd.niceteam.domain.member.Member;
import com.dnd.niceteam.domain.member.MemberRepository;
import com.dnd.niceteam.domain.memberscore.MemberScore;
import com.dnd.niceteam.domain.memberscore.MemberScoreRepository;
import com.dnd.niceteam.domain.project.LectureProject;
import com.dnd.niceteam.domain.project.LectureProjectRepository;
import com.dnd.niceteam.domain.project.LectureTime;
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

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import({TestJpaConfig.class, CommentService.class})
@Transactional
class CommentServiceTest {
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
    private CommentService commentService;
    @Autowired
    private CommentRepository commentRepository;

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
                .totalParticipationScore(0)
                .totalTeamAgainScore(0)
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
                .lectureTimes(Set.of(LectureTime.builder().dayOfWeek(DayOfWeek.MON).startTime(LocalTime.of(9, 0)).build()))
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
    @DisplayName("신규 댓글을 작성합니다.")
    @Test
    void create() {
        //given
        CommentCreation.RequestDto request = new CommentCreation.RequestDto();
        request.setContent("모집글의 댓글입니다.");
        request.setParentId(0L);
        Comment dtoToComment = request.toEntity(member, recruiting);

        // when
        CommentCreation.ResponseDto response = commentService.addComment(recruiting.getId(), account.getEmail(), request);

        //then
        Comment createdComment = commentRepository.findById(response.getId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 댓글"));
        Recruiting updatedRecruiting = recruitingRepository.findById(recruiting.getId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 모집글"));

        assertThat(response.getId()).isEqualTo(createdComment.getId());
        assertThat(dtoToComment.getContent()).isEqualTo(createdComment.getContent());
        assertThat(dtoToComment.getParentId()).isEqualTo(createdComment.getParentId());
        assertThat(updatedRecruiting.getCommentCount()).isEqualTo(1);
    }

}