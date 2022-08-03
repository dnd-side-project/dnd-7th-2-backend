package com.dnd.niceteam.member.service;

import com.dnd.niceteam.common.TestJpaConfig;
import com.dnd.niceteam.domain.account.Account;
import com.dnd.niceteam.domain.account.AccountRepository;
import com.dnd.niceteam.domain.code.Personality;
import com.dnd.niceteam.domain.department.Department;
import com.dnd.niceteam.domain.department.DepartmentRepository;
import com.dnd.niceteam.domain.emailauth.EmailAuthRepository;
import com.dnd.niceteam.domain.member.Member;
import com.dnd.niceteam.domain.member.MemberRepository;
import com.dnd.niceteam.domain.university.University;
import com.dnd.niceteam.domain.university.UniversityRepository;
import com.dnd.niceteam.member.dto.DupCheck;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.assertThat;

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
}