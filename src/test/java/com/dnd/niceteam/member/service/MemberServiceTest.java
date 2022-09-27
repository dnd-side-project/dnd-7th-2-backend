package com.dnd.niceteam.member.service;

import com.dnd.niceteam.common.TestJpaConfig;
import com.dnd.niceteam.domain.account.Account;
import com.dnd.niceteam.domain.account.AccountRepository;
import com.dnd.niceteam.domain.code.Field;
import com.dnd.niceteam.domain.code.Personality;
import com.dnd.niceteam.domain.department.Department;
import com.dnd.niceteam.domain.department.DepartmentRepository;
import com.dnd.niceteam.domain.emailauth.EmailAuth;
import com.dnd.niceteam.domain.emailauth.EmailAuthRepository;
import com.dnd.niceteam.domain.emailauth.exception.NotAuthenticatedEmail;
import com.dnd.niceteam.domain.member.Member;
import com.dnd.niceteam.domain.member.MemberRepository;
import com.dnd.niceteam.domain.member.exception.DuplicateEmailException;
import com.dnd.niceteam.domain.member.exception.DuplicateNicknameException;
import com.dnd.niceteam.domain.member.exception.MemberNotFoundException;
import com.dnd.niceteam.domain.memberscore.MemberScore;
import com.dnd.niceteam.domain.memberscore.MemberScoreRepository;
import com.dnd.niceteam.domain.project.ProjectMember;
import com.dnd.niceteam.domain.project.ProjectMemberRepository;
import com.dnd.niceteam.domain.university.University;
import com.dnd.niceteam.domain.university.UniversityRepository;
import com.dnd.niceteam.member.dto.DupCheck;
import com.dnd.niceteam.member.dto.MemberCreation;
import com.dnd.niceteam.member.dto.MemberDetail;
import com.dnd.niceteam.member.dto.MemberUpdate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@DataJpaTest
@Import({TestJpaConfig.class, MemberService.class})
@Transactional
class MemberServiceTest {

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private EmailAuthRepository emailAuthRepository;

    @Autowired
    private UniversityRepository universityRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private MemberScoreRepository memberScoreRepository;

    @Autowired
    private ProjectMemberRepository projectMemberRepository;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EntityManager em;

    @Test
    @DisplayName("이메일 중복 확인 -> 중복 존재")
    void checkEmailDuplicate_Duplicate_Success() {
        // given
        accountRepository.save(Account.builder()
                .email("test@email.com")
                .password("testPassword123!@#")
                .build());
        em.flush();
        em.clear();

        // when
        DupCheck.ResponseDto responseDto = memberService.checkEmailDuplicate("test@email.com");

        // then
        assertThat(responseDto.getDuplicated()).isTrue();
    }

    @Test
    @DisplayName("이메일 중복 확인 -> 중복 없음")
    void checkEmailDuplicate_NonDuplicate_Success() {
        // given

        // when
        DupCheck.ResponseDto responseDto = memberService.checkEmailDuplicate("test@email.com");

        // then
        assertThat(responseDto.getDuplicated()).isFalse();
    }

    @Test
    @DisplayName("닉네임 중복 확인 -> 중복 존재")
    void checkNicknameDuplicate_Duplicate_Success() {
        // given
        University university = universityRepository.save(University.builder()
                .name("테스트대학교")
                .emailDomain("email.com")
                .build());
        Department department = departmentRepository.save(Department.builder()
                .university(university)
                .collegeName("단과대학")
                .name("학과")
                .region("서울")
                .mainBranchType("본교")
                .build());
        MemberScore memberScore = memberScoreRepository.save(MemberScore.builder()
                .level(1)
                .reviewNum(0)
                .totalParticipationScore(0)
                .totalTeamAgainScore(0)
                .build());
        Account account = accountRepository.save(Account.builder()
                .email("test@email.com")
                .password("testPassword123!@#")
                .build());
        Member member = memberRepository.save(Member.builder()
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
        em.flush();
        em.clear();

        // when
        DupCheck.ResponseDto responseDto = memberService.checkNicknameDuplicate("테스트닉네임");

        // then
        assertThat(responseDto.getDuplicated()).isTrue();
    }

    @Test
    @DisplayName("닉네임 중복 확인 - 중복 없음")
    void checkNicknameDuplicate_NonDuplicate_Success() {
        // given

        // when
        DupCheck.ResponseDto responseDto = memberService.checkNicknameDuplicate("테스트닉네임");

        // then
        assertThat(responseDto.getDuplicated()).isFalse();
    }

    @Test
    @DisplayName("회원가입")
    void createMember() {
        // given
        emailAuthRepository.save(EmailAuth.builder()
                .email("test@email.com")
                .authKey("123456")
                .authenticated(true)
                .build());
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
        MemberCreation.RequestDto requestDto = new MemberCreation.RequestDto();
        requestDto.setEmail("test@email.com");
        requestDto.setPassword("testPassword123!@#");
        requestDto.setNickname("test-nickname");
        requestDto.setPersonalityAdjective(Personality.Adjective.LOGICAL);
        requestDto.setPersonalityNoun(Personality.Noun.LEADER);
        requestDto.setInterestingFields(Set.of(Field.IT_SW_GAME, Field.PLANNING_IDEA));
        requestDto.setDepartmentId(department.getId());
        requestDto.setAdmissionYear(2017);
        requestDto.setIntroduction("자기소개");
        requestDto.setIntroductionUrl("자기소개 링크");
        em.flush();
        em.clear();
        given(passwordEncoder.encode(anyString())).willReturn("encoded-password");

        // when
        MemberCreation.ResponseDto responseDto = memberService.createMember(requestDto);

        // then
        Member member = memberRepository.findByEmail("test@email.com")
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원"));
        assertThat(responseDto.getId()).isEqualTo(member.getId());
        assertThat(responseDto.getEmail()).isEqualTo("test@email.com");
        assertThat(member.getAccount().getEmail()).isEqualTo("test@email.com");
        assertThat(member.getUniversity().getName()).isEqualTo("테스트대학교");
        assertThat(member.getDepartment().getName()).isEqualTo("테스트학과");
        assertThat(member.getNickname()).isEqualTo("test-nickname");
        assertThat(member.getPersonality().getAdjective()).isEqualTo(Personality.Adjective.LOGICAL);
        assertThat(member.getPersonality().getNoun()).isEqualTo(Personality.Noun.LEADER);
        assertThat(member.getInterestingFields()).containsOnly(Field.IT_SW_GAME, Field.PLANNING_IDEA);
    }

    @Test
    @DisplayName("회원가입 - 인증되지 않은 이메일")
    void createMember_NotAuthEmail() {
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
        MemberCreation.RequestDto requestDto = new MemberCreation.RequestDto();
        requestDto.setEmail("test@email.com");
        requestDto.setPassword("testPassword123!@#");
        requestDto.setNickname("test-nickname");
        requestDto.setPersonalityAdjective(Personality.Adjective.LOGICAL);
        requestDto.setPersonalityNoun(Personality.Noun.LEADER);
        requestDto.setInterestingFields(Set.of(Field.IT_SW_GAME, Field.PLANNING_IDEA));
        requestDto.setDepartmentId(department.getId());
        requestDto.setAdmissionYear(2017);
        requestDto.setIntroduction("자기소개");
        requestDto.setIntroductionUrl("자기소개 링크");
        em.flush();
        em.clear();
        given(passwordEncoder.encode(anyString())).willReturn("encoded-password");

        // expected
        assertThatThrownBy(() -> memberService.createMember(requestDto))
                .isInstanceOf(NotAuthenticatedEmail.class);
    }

    @Test
    @DisplayName("회원가입 - 중복된 이메일")
    void createMember_DuplicateEmail() {
        // given
        emailAuthRepository.save(EmailAuth.builder()
                .email("test@email.com")
                .authKey("123456")
                .authenticated(true)
                .build());
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
                .totalParticipationScore(0)
                .totalTeamAgainScore(0)
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
                .nickname("nickname")
                .admissionYear(2017)
                .personality(new Personality(Personality.Adjective.LOGICAL, Personality.Noun.LEADER))
                .introduction("")
                .introductionUrl("")
                .build());
        MemberCreation.RequestDto requestDto = new MemberCreation.RequestDto();
        requestDto.setEmail("test@email.com");
        requestDto.setPassword("testPassword123!@#");
        requestDto.setNickname("test-nickname");
        requestDto.setPersonalityAdjective(Personality.Adjective.LOGICAL);
        requestDto.setPersonalityNoun(Personality.Noun.LEADER);
        requestDto.setInterestingFields(Set.of(Field.IT_SW_GAME, Field.PLANNING_IDEA));
        requestDto.setDepartmentId(department.getId());
        requestDto.setAdmissionYear(2017);
        requestDto.setIntroduction("자기소개");
        requestDto.setIntroductionUrl("자기소개 링크");
        em.flush();
        em.clear();
        given(passwordEncoder.encode(anyString())).willReturn("encoded-password");

        // when
        assertThatThrownBy(() -> memberService.createMember(requestDto))
                .isInstanceOf(DuplicateEmailException.class);
    }

    @Test
    @DisplayName("회원가입 - 중복된 닉네임")
    void createMember_DuplicateNickname() {
        // given
        emailAuthRepository.save(EmailAuth.builder()
                .email("test@email.com")
                .authKey("123456")
                .authenticated(true)
                .build());
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
                .totalParticipationScore(0)
                .totalTeamAgainScore(0)
                .build());
        Account account = accountRepository.save(Account.builder()
                .email("notduplicate@email.com")
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
        MemberCreation.RequestDto requestDto = new MemberCreation.RequestDto();
        requestDto.setEmail("test@email.com");
        requestDto.setPassword("testPassword123!@#");
        requestDto.setNickname("test-nickname");
        requestDto.setPersonalityAdjective(Personality.Adjective.LOGICAL);
        requestDto.setPersonalityNoun(Personality.Noun.LEADER);
        requestDto.setInterestingFields(Set.of(Field.IT_SW_GAME, Field.PLANNING_IDEA));
        requestDto.setDepartmentId(department.getId());
        requestDto.setAdmissionYear(2017);
        requestDto.setIntroduction("자기소개");
        requestDto.setIntroductionUrl("자기소개 링크");
        em.flush();
        em.clear();
        given(passwordEncoder.encode(anyString())).willReturn("encoded-password");

        // when
        assertThatThrownBy(() -> memberService.createMember(requestDto))
                .isInstanceOf(DuplicateNicknameException.class);
    }

    @Test
    @DisplayName("회원 수정")
    void updateMember() {
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
                .totalParticipationScore(0)
                .totalTeamAgainScore(0)
                .build());
        Account account = accountRepository.save(Account.builder()
                .email("test@email.com")
                .password("Password123!@#")
                .build());
        Member member = memberRepository.save(Member.builder()
                .account(account)
                .university(university)
                .department(department)
                .memberScore(memberScore)
                .nickname("테스트닉네임")
                .admissionYear(2017)
                .personality(new Personality(Personality.Adjective.LOGICAL, Personality.Noun.LEADER))
                .interestingFields(Set.of(Field.DESIGN))
                .introduction("")
                .introductionUrl("")
                .build());
        MemberUpdate.RequestDto requestDto = new MemberUpdate.RequestDto();
        requestDto.setNickname("변경된닉네임");
        requestDto.setPersonalityAdjective(Personality.Adjective.PERSISTENT);
        requestDto.setPersonalityNoun(Personality.Noun.INVENTOR);
        requestDto.setInterestingFields(Set.of(Field.IT_SW_GAME, Field.PLANNING_IDEA));
        requestDto.setIntroduction("변경된 자기소개");
        requestDto.setIntroductionUrl("변경된 자기소개 링크");
        em.flush();
        em.clear();

        // when
        MemberUpdate.ResponseDto responseDto = memberService.updateMember("test@email.com", requestDto);

        // given
        Member foundMember = memberRepository.findByEmail("test@email.com")
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 멤버"));
        assertThat(foundMember.getNickname()).isEqualTo("변경된닉네임");
        assertThat(foundMember.getPersonality().getAdjective()).isEqualTo(Personality.Adjective.PERSISTENT);
        assertThat(foundMember.getPersonality().getNoun()).isEqualTo(Personality.Noun.INVENTOR);
        assertThat(foundMember.getInterestingFields()).containsOnly(Field.IT_SW_GAME, Field.PLANNING_IDEA);
        assertThat(foundMember.getIntroduction()).isEqualTo("변경된 자기소개");
        assertThat(foundMember.getIntroductionUrl()).isEqualTo("변경된 자기소개 링크");
    }

    @Test
    @DisplayName("회원 수정 - 존재하지 않는 회원")
    void updateMember_MemberNotExists() {
        // given
        MemberUpdate.RequestDto requestDto = new MemberUpdate.RequestDto();
        requestDto.setNickname("변경된닉네임");
        requestDto.setPersonalityAdjective(Personality.Adjective.PERSISTENT);
        requestDto.setPersonalityNoun(Personality.Noun.INVENTOR);
        requestDto.setInterestingFields(Set.of(Field.IT_SW_GAME, Field.PLANNING_IDEA));
        requestDto.setIntroduction("변경된 자기소개");
        requestDto.setIntroductionUrl("변경된 자기소개 링크");
        em.flush();
        em.clear();

        // expected
        assertThatThrownBy(() -> memberService.updateMember("test@email.com", requestDto))
                .isInstanceOf(MemberNotFoundException.class);
    }

    @Test
    @DisplayName("회원 상세")
    void getMemberDetail() {
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
                .totalParticipationScore(0)
                .totalTeamAgainScore(0)
                .build());
        Account account = accountRepository.save(Account.builder()
                .email("test@email.com")
                .password("Password123!@#")
                .build());
        Member member = memberRepository.save(Member.builder()
                .account(account)
                .university(university)
                .department(department)
                .memberScore(memberScore)
                .nickname("테스트닉네임")
                .admissionYear(2017)
                .personality(new Personality(Personality.Adjective.LOGICAL, Personality.Noun.LEADER))
                .interestingFields(Set.of(Field.DESIGN))
                .introduction("테스트 자기소개")
                .introductionUrl("test.com")
                .build());
        em.flush();
        em.clear();

        // when
        MemberDetail.ResponseDto responseDto = memberService.getMemberDetail(member.getId());

        // given
        List<ProjectMember> projectMembers = projectMemberRepository.findDoneProjectMemberByMember(member);
        assertThat(responseDto.getId()).isEqualTo(member.getId());
        assertThat(responseDto.getNickname()).isEqualTo(member.getNickname());
        assertThat(responseDto.getPersonality().getAdjective()).isEqualTo(member.getPersonality().getAdjective());
        assertThat(responseDto.getPersonality().getNoun()).isEqualTo(member.getPersonality().getNoun());
        assertThat(responseDto.getDepartmentName()).isEqualTo(member.getDepartment().getName());
        assertThat(responseDto.getInterestingFields()).isEqualTo(member.getInterestingFields());
        assertThat(responseDto.getAdmissionYear()).isEqualTo(member.getAdmissionYear());
        assertThat(responseDto.getIntroduction()).isEqualTo(member.getIntroduction());
        assertThat(responseDto.getIntroductionUrl()).isEqualTo(member.getIntroductionUrl());
        assertThat(responseDto.getLevel()).isEqualTo(memberScore.getLevel());
        assertThat(responseDto.getParticipationPct()).isEqualTo(memberScore.participationPct());
        assertThat(responseDto.getReviewTagToNums()).isEqualTo(memberScore.getReviewTagToNums());
        assertThat(responseDto.getNumTotalEndProject()).isEqualTo(projectMembers.size());
        assertThat(responseDto.getNumCompleteProject()).isEqualTo(
                (int) projectMembers.stream().filter(projectMember -> !projectMember.getExpelled()).count());
    }

    @Test
    @DisplayName("본인 회원 상세")
    void getMyMemberDetail() {
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
                .totalParticipationScore(0)
                .totalTeamAgainScore(0)
                .build());
        Account account = accountRepository.save(Account.builder()
                .email("test@email.com")
                .password("Password123!@#")
                .build());
        Member member = memberRepository.save(Member.builder()
                .account(account)
                .university(university)
                .department(department)
                .memberScore(memberScore)
                .nickname("테스트닉네임")
                .admissionYear(2017)
                .personality(new Personality(Personality.Adjective.LOGICAL, Personality.Noun.LEADER))
                .interestingFields(Set.of(Field.DESIGN))
                .introduction("테스트 자기소개")
                .introductionUrl("test.com")
                .build());
        em.flush();
        em.clear();

        // when
        MemberDetail.ResponseDto responseDto = memberService.getMyMemberDetail(member.getAccount().getEmail());

        // given
        List<ProjectMember> projectMembers = projectMemberRepository.findDoneProjectMemberByMember(member);
        assertThat(responseDto.getId()).isEqualTo(member.getId());
        assertThat(responseDto.getNickname()).isEqualTo(member.getNickname());
        assertThat(responseDto.getPersonality().getAdjective()).isEqualTo(member.getPersonality().getAdjective());
        assertThat(responseDto.getPersonality().getNoun()).isEqualTo(member.getPersonality().getNoun());
        assertThat(responseDto.getDepartmentName()).isEqualTo(member.getDepartment().getName());
        assertThat(responseDto.getInterestingFields()).isEqualTo(member.getInterestingFields());
        assertThat(responseDto.getAdmissionYear()).isEqualTo(member.getAdmissionYear());
        assertThat(responseDto.getIntroduction()).isEqualTo(member.getIntroduction());
        assertThat(responseDto.getIntroductionUrl()).isEqualTo(member.getIntroductionUrl());
        assertThat(responseDto.getLevel()).isEqualTo(memberScore.getLevel());
        assertThat(responseDto.getParticipationPct()).isEqualTo(memberScore.participationPct());
        assertThat(responseDto.getReviewTagToNums()).isEqualTo(memberScore.getReviewTagToNums());
        assertThat(responseDto.getNumTotalEndProject()).isEqualTo(projectMembers.size());
        assertThat(responseDto.getNumCompleteProject()).isEqualTo(
                (int) projectMembers.stream().filter(projectMember -> !projectMember.getExpelled()).count());
    }

    @Test
    @DisplayName("회원 상세 - 존재하지 않는 회원")
    void getMemberDetail_MemberNotExists() {
        // expected
        assertThatThrownBy(() -> memberService.getMemberDetail(1L))
                .isInstanceOf(MemberNotFoundException.class);
    }
}