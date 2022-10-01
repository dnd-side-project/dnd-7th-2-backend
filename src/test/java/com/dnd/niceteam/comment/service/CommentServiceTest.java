package com.dnd.niceteam.comment.service;

import com.dnd.niceteam.comment.dto.*;
import com.dnd.niceteam.common.TestJpaConfig;
import com.dnd.niceteam.common.dto.Pagination;
import com.dnd.niceteam.domain.account.Account;
import com.dnd.niceteam.domain.account.AccountRepository;
import com.dnd.niceteam.domain.code.*;
import com.dnd.niceteam.domain.comment.Comment;
import com.dnd.niceteam.domain.comment.CommentRepository;
import com.dnd.niceteam.domain.comment.exception.CommentNotFoundException;
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
import com.dnd.niceteam.domain.recruiting.RecruitingStatus;
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
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

import static com.dnd.niceteam.comment.EntityFactoryForTest.createComment;
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
                .status(RecruitingStatus.IN_PROGRESS)
                .commentCount(0)
                .bookmarkCount(0)
                .poolUpCount(0)
                .introLink("test-introLink")
                .build()
        );

        em.flush();
        em.clear();
    }
    @DisplayName("신규 댓글 작성")
    @Test
    void create() {
        //given
        CommentCreation.RequestDto parent1RequestDto = new CommentCreation.RequestDto();
        parent1RequestDto.setContent("모집글의 댓글입니다.");
        parent1RequestDto.setParentId(0L);
        CommentCreation.ResponseDto parentResponse = commentService.addComment(recruiting.getId(), account.getEmail(), parent1RequestDto);

        CommentCreation.RequestDto childRequestDtoFromParent1 = new CommentCreation.RequestDto();
        childRequestDtoFromParent1.setContent("모집글의 첫번째 댓글의 답글입니다.");
        childRequestDtoFromParent1.setParentId(parentResponse.getId());

        CommentCreation.RequestDto parentRequestDto2 = new CommentCreation.RequestDto();
        parentRequestDto2.setContent("모집글의 두번째 댓글입니다.");
        parentRequestDto2.setParentId(0L);
        CommentCreation.ResponseDto parentResponse2 = commentService.addComment(recruiting.getId(), account.getEmail(), parentRequestDto2);

        // when
        CommentCreation.ResponseDto childResponseFromParent1 = commentService.addComment(recruiting.getId(), account.getEmail(), childRequestDtoFromParent1);

        //then
        Comment foundParentComment = commentRepository.findById(parentResponse.getId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 댓글"));
        Comment foundChildComment = commentRepository.findById(childResponseFromParent1.getId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 댓글"));
        Recruiting foundRecruitingWithComment = recruitingRepository.findById(recruiting.getId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 모집글"));

        assertThat(parentResponse.getId()).isEqualTo(foundParentComment.getId());
        assertThat(foundChildComment.getContent()).isEqualTo(childRequestDtoFromParent1.getContent());
        assertThat(childResponseFromParent1.getParentId()).isEqualTo(parentResponse.getId());
        assertThat(parentResponse.getParentId()).isEqualTo(0L);
        assertThat(foundRecruitingWithComment.getCommentCount()).isEqualTo(3);
        // groupNo 체크
        assertThat(parentResponse2.getGroupNo()).isNotEqualTo(parentResponse.getGroupNo());
        assertThat(parentResponse.getGroupNo()).isEqualTo(childResponseFromParent1.getGroupNo());
    }

    @DisplayName("신규 댓글 제거")
    @Test
    void remove() {
        // given
        CommentCreation.RequestDto parentRequestDto = new CommentCreation.RequestDto();
        parentRequestDto.setContent("모집글의 댓글입니다.");
        parentRequestDto.setParentId(0L);
        CommentCreation.ResponseDto parentResponse = commentService.addComment(recruiting.getId(), account.getEmail(), parentRequestDto);

        CommentCreation.RequestDto childRequestDto = new CommentCreation.RequestDto();
        childRequestDto.setContent("모집글의 답글입니다.");
        childRequestDto.setParentId(parentResponse.getId());
        commentService.addComment(recruiting.getId(), account.getEmail(), childRequestDto);

        // when
        commentService.removeComment(parentResponse.getId(), account.getEmail());

        // then : 모댓글 1개만 제거됐는지 체크
        Recruiting updatedRecruiting = recruitingRepository.findById(recruiting.getId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 모집글"));
        assertThat(updatedRecruiting.getCommentCount()).isEqualTo(1);

        RuntimeException e = Assertions.assertThrows(CommentNotFoundException.class
                , () -> commentRepository.findById(parentResponse.getId())
                        .orElseThrow(() -> new CommentNotFoundException("존재하지 않는 댓글")));
        assertThat(e.getMessage()).startsWith(ErrorCode.COMMENT_NOT_FOUND.name());
    }


    @DisplayName("신규 댓글 수정")
    @Test
    void modify() {
        // given
        Comment comment = createComment(member, recruiting);
        Comment savedComment = commentRepository.save(comment);

        // when
        CommentModify.RequestDto commentModifyRequestDto = new CommentModify.RequestDto();
        commentModifyRequestDto.setId(savedComment.getId());
        commentModifyRequestDto.setContent("updated-comment!!");
        CommentModify.ResponseDto modifiedCommentResponseDto = commentService.modifyComment(
                commentModifyRequestDto, account.getEmail());

        // then
        Comment foundModifiedComment = commentRepository.findById(modifiedCommentResponseDto.getId())
                .orElseThrow(() -> new CommentNotFoundException("존재하지 않는 댓글"));
        assertThat(commentModifyRequestDto.getContent()).isEqualTo(foundModifiedComment.getContent());
        assertThat(foundModifiedComment.getContent()).isNotEqualTo("create-comment");
        assertThat(modifiedCommentResponseDto.getGroupNo()).isEqualTo(savedComment.getGroupNo());
    }

    @DisplayName("모집글의 댓글 목록 조회")
    @Test
    void recruitingsCommentList() {
        // given
        CommentCreation.RequestDto parentRequestDto1 = new CommentCreation.RequestDto();
        parentRequestDto1.setContent("모집글의 첫번째 댓글입니다.");
        parentRequestDto1.setParentId(0L);
        CommentCreation.ResponseDto parentResponse1 = commentService.addComment(
                recruiting.getId(), account.getEmail(), parentRequestDto1);

        CommentCreation.RequestDto parentRequestDto2 = new CommentCreation.RequestDto();
        parentRequestDto2.setContent("모집글의 두번째 댓글입니다.");
        parentRequestDto2.setParentId(0L);
        CommentCreation.ResponseDto parentResponse2 = commentService.addComment(
                recruiting.getId(), account.getEmail(), parentRequestDto2);

        CommentCreation.RequestDto childRequestDto1 = new CommentCreation.RequestDto();
        childRequestDto1.setContent("모집글의 첫번째 댓글의 답글입니다.");
        childRequestDto1.setParentId(parentResponse1.getId());
        CommentCreation.ResponseDto childResponse1 = commentService.addComment(
                recruiting.getId(), account.getEmail(), childRequestDto1);

        CommentCreation.RequestDto childRequestDto2 = new CommentCreation.RequestDto();
        childRequestDto2.setContent("모집글의 두번째 댓글의 답글입니다.");
        childRequestDto2.setParentId(parentResponse2.getId());
        CommentCreation.ResponseDto childResponse2 = commentService.addComment(
                recruiting.getId(), account.getEmail(), childRequestDto2);
        CommentCreation.RequestDto childRequestDto3 = new CommentCreation.RequestDto();
        childRequestDto3.setContent("모집글의 두번째 댓글의 두번째 답글입니다.");
        childRequestDto3.setParentId(parentResponse2.getId());
        CommentCreation.ResponseDto childResponse3 = commentService.addComment(
                recruiting.getId(), account.getEmail(), childRequestDto3);

        // when
        Pagination<CommentFind.ResponseDto> commentfirstPage = commentService.getComments(1, 4, recruiting.getId(), null);
        Pagination<CommentFind.ResponseDto> commentsecondPage = commentService.getComments(2, 4, recruiting.getId(), null);
        // then
        assertThat(commentfirstPage.getPage()).isZero();
        assertThat(commentfirstPage.getTotalPages()).isEqualTo(2);
        assertThat(commentfirstPage.getContents().size()).isEqualTo(4);
        assertThat(commentfirstPage.getContents().get(0).getCommentId()).isEqualTo(parentResponse1.getId());
        assertThat(commentfirstPage.getContents().get(1).getCommentId()).isEqualTo(childResponse1.getId());
        assertThat(commentfirstPage.getContents().get(2).getCommentId()).isEqualTo(parentResponse2.getId());
        assertThat(commentfirstPage.getContents().get(3).getCommentId()).isEqualTo(childResponse2.getId());
        assertThat(commentsecondPage.getContents().get(0).getCommentId()).isEqualTo(childResponse3.getId());
    }

    @DisplayName("내가 쓴 댓글 목록 조회")
    @Test
    void myCommentList() {
        // gieven
        CommentCreation.RequestDto myCommentRequestDto1 = new CommentCreation.RequestDto();
        myCommentRequestDto1.setContent("모집글의 첫번째 댓글입니다.");
        myCommentRequestDto1.setParentId(0L);
        CommentCreation.RequestDto myCommentRequestDto2 = new CommentCreation.RequestDto();
        myCommentRequestDto2.setContent("모집글의 두번째 댓글입니다.");
        myCommentRequestDto2.setParentId(0L);

        CommentCreation.ResponseDto myCommentResponse = commentService.addComment(
                recruiting.getId(), account.getEmail(), myCommentRequestDto1);
        CommentCreation.ResponseDto myCommentResponse2 = commentService.addComment(
                recruiting.getId(), account.getEmail(), myCommentRequestDto2);

        //다른 멤버
        University university2 = universityRepository.save(University.builder()
                .name("테스트대학교")
                .emailDomain("email.com")
                .build());
        Department department2 = departmentRepository.save(Department.builder()
                .university(university2)
                .collegeName("단과대학")
                .name("학과")
                .region("서울")
                .mainBranchType("본교")
                .build());
        MemberScore memberScore2 = memberScoreRepository.save(MemberScore.builder()
                .level(1)
                .reviewNum(0)
                .totalParticipationScore(0)
                .totalTeamAgainScore(0)
                .build());
        Account account2 = accountRepository.save(Account.builder()
                .email("test2@email.com")
                .password("test2Password123!@#")
                .build());
        memberRepository.save(Member.builder()
                .account(account2)
                .university(university2)
                .department(department2)
                .memberScore(memberScore2)
                .nickname("테스트2닉네임")
                .admissionYear(2017)
                .personality(new Personality(Personality.Adjective.LOGICAL, Personality.Noun.LEADER))
                .introduction("")
                .introductionUrl("")
                .build());

        CommentCreation.RequestDto otherCommentRequestDto = new CommentCreation.RequestDto();
        otherCommentRequestDto.setContent("다른 회원이 작성한 모집글의 두번째 댓글의 답글입니다.");
        otherCommentRequestDto.setParentId(myCommentResponse2.getId());
        commentService.addComment(recruiting.getId(), account2.getEmail(), otherCommentRequestDto);

        // when
        commentService.removeComment(myCommentResponse2.getId(), account.getEmail());
        recruitingRepository.delete(recruiting);
        Pagination<CommentFind.ResponseDto> myCommentsPage = commentService.getComments(
                1, 2, recruiting.getId(), account.getEmail());

        // then
        assertThat(myCommentsPage.getPage()).isZero();
        assertThat(myCommentsPage.getTotalPages()).isEqualTo(1);
        assertThat(myCommentsPage.getContents().size()).isEqualTo(1);
        assertThat(myCommentsPage.getContents().get(0).getCommentId()).isEqualTo(myCommentResponse.getId());
    }
}