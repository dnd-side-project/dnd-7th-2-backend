package com.dnd.niceteam.member.service;

import com.dnd.niceteam.common.TestJpaConfig;
import com.dnd.niceteam.domain.account.Account;
import com.dnd.niceteam.domain.account.AccountRepository;
import com.dnd.niceteam.domain.emailauth.EmailAuthRepository;
import com.dnd.niceteam.domain.member.MemberRepository;
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
    private EntityManager em;

    @Test
    @DisplayName("이메일 중복 확인 -> 중복 존재 - 성공")
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
    @DisplayName("이메일 중복 확인 -> 중복 없음 - 성공")
    void checkEmailDuplicate_NonDuplicate_Success() {
        // given

        // when
        DupCheck.ResponseDto responseDto = memberService.checkEmailDuplicate("test@email.com");

        // then
        assertThat(responseDto.getDuplicated()).isFalse();
    }
}