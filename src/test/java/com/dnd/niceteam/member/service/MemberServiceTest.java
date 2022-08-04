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
import com.dnd.niceteam.domain.university.University;
import com.dnd.niceteam.domain.university.UniversityRepository;
import com.dnd.niceteam.member.dto.DupCheck;
import com.dnd.niceteam.member.dto.MemberCreation;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

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
        Account account = accountRepository.save(Account.builder()
                .email("test@email.com")
                .password("testPassword123!@#")
                .build());
        Member member = memberRepository.save(Member.builder()
                .account(account)
                .university(university)
                .department(department)
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
        Account account = accountRepository.save(Account.builder()
                .email("test@email.com")
                .password("test-password")
                .build());
        Member member = memberRepository.save(Member.builder()
                .account(account)
                .university(university)
                .department(department)
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
        Account account = accountRepository.save(Account.builder()
                .email("notduplicate@email.com")
                .password("test-password")
                .build());
        Member member = memberRepository.save(Member.builder()
                .account(account)
                .university(university)
                .department(department)
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
}