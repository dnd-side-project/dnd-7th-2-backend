package com.dnd.niceteam.emailauth.service;

import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.dnd.niceteam.common.TestJpaConfig;
import com.dnd.niceteam.domain.emailauth.EmailAuth;
import com.dnd.niceteam.domain.emailauth.EmailAuthRepository;
import com.dnd.niceteam.domain.emailauth.exception.EmailAuthNotFoundException;
import com.dnd.niceteam.domain.university.University;
import com.dnd.niceteam.domain.university.UniversityRepository;
import com.dnd.niceteam.domain.university.exception.InvalidEmailDomainException;
import com.dnd.niceteam.domain.university.exception.UniversityNotFoundException;
import com.dnd.niceteam.emailauth.dto.EmailAuthKeyCheckRequestDto;
import com.dnd.niceteam.emailauth.dto.EmailAuthKeyCheckResponseDto;
import com.dnd.niceteam.emailauth.dto.EmailAuthKeySendRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@DataJpaTest
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@Import(TestJpaConfig.class)
@Transactional
class EmailAuthServiceTest {

    private EmailAuthService emailAuthService;

    @Autowired
    private EmailAuthRepository emailAuthRepository;

    @Autowired
    private UniversityRepository universityRepository;

    @Mock
    private AmazonSimpleEmailService mockEmailService;

    @Mock
    private EmailAuthKeyCreator mockKeyCreator;

    @Autowired
    private EntityManager em;

    @BeforeEach
    void setup() {
        emailAuthService = new EmailAuthService(
                mockEmailService, emailAuthRepository, universityRepository, mockKeyCreator);
    }

    @Test
    @DisplayName("이메일 인증번호 요청 - 성공")
    void sendEmailAuthKey_Success() {
        // given
        universityRepository.save(University.builder()
                .name("팀구대학교")
                .emailDomain("teamgoo.com")
                .build());
        EmailAuthKeySendRequestDto requestDto = new EmailAuthKeySendRequestDto();
        requestDto.setEmail("test@teamgoo.com");
        requestDto.setUniversityId(1L);
        given(mockKeyCreator.createEmailAuthKey()).willReturn("123456");

        em.flush();
        em.clear();

        // when
        emailAuthService.sendEmailAuthKey(requestDto);

        // then
        EmailAuth emailAuth = emailAuthRepository.findLatestByEmail("test@teamgoo.com")
                .orElseThrow(() -> new IllegalArgumentException("Email not found."));
        assertThat(emailAuth.getAuthKey()).isEqualTo("123456");
    }

    @Test
    @DisplayName("이메일 인증번호 요청 -> 없는 대학교 이름으로 - 실패")
    void sendEmailAuthKey_NotFoundUniv_ThenFail() {
        // given
        University university = universityRepository.save(University.builder()
                .name("팀구대학교")
                .emailDomain("teamgoo.com")
                .build());
        EmailAuthKeySendRequestDto requestDto = new EmailAuthKeySendRequestDto();
        requestDto.setEmail("test@teamgoo.com");
        requestDto.setUniversityId(university.getId() + 1L);
        given(mockKeyCreator.createEmailAuthKey()).willReturn("123456");

        em.flush();
        em.clear();

        // expected
        assertThatThrownBy(() -> emailAuthService.sendEmailAuthKey(requestDto))
                .isInstanceOf(UniversityNotFoundException.class);
    }

    @Test
    @DisplayName("이메일 인증번호 요청 -> 일치하지 않는 이메일 도메인 - 실패")
    void sendEmailAuthKey_InvalidEmail_ThenFail() {
        // given
        University university = universityRepository.save(University.builder()
                .name("팀구대학교")
                .emailDomain("teamgoo.com")
                .build());
        EmailAuthKeySendRequestDto requestDto = new EmailAuthKeySendRequestDto();
        requestDto.setEmail("test@invalidemail.com");
        requestDto.setUniversityId(university.getId());
        given(mockKeyCreator.createEmailAuthKey()).willReturn("123456");

        em.flush();
        em.clear();

        // expected
        assertThatThrownBy(() -> emailAuthService.sendEmailAuthKey(requestDto))
                .isInstanceOf(InvalidEmailDomainException.class);
    }

    @Test
    @DisplayName("이메일 인증번호 확인 - 성공")
    void checkEmailAuthKey_Success() {
        // given
        emailAuthRepository.save(EmailAuth.builder()
                .email("test@teamgoo.com")
                .authKey("123456")
                .build());
        EmailAuthKeyCheckRequestDto requestDto = new EmailAuthKeyCheckRequestDto();
        requestDto.setEmail("test@teamgoo.com");
        requestDto.setAuthKey("123456");
        em.flush();
        em.clear();

        // when
        EmailAuthKeyCheckResponseDto responseDto = emailAuthService.checkEmailAuthKey(requestDto);

        // then
        assertThat(responseDto.getEmail()).isEqualTo("test@teamgoo.com");
        assertThat(responseDto.getAuthenticated()).isTrue();

        EmailAuth emailAuth = emailAuthRepository.findLatestByEmail("test@teamgoo.com")
                .orElseThrow(() -> new IllegalArgumentException("인증번호가 발급되지 않음."));
        assertThat(emailAuth.getEmail()).isEqualTo("test@teamgoo.com");
        assertThat(emailAuth.getAuthKey()).isEqualTo("123456");
        assertThat(emailAuth.getAuthenticated()).isTrue();
    }

    @Test
    @DisplayName("이메일 인증번호 확인 -> 인증번호가 발급되지 않은 이메일 - 실패")
    void checkEmailAuthKey_NotFoundEmailAuth_ThenFail() {
        // given
        emailAuthRepository.save(EmailAuth.builder()
                .email("test@teamgoo.com")
                .authKey("123456")
                .build());
        EmailAuthKeyCheckRequestDto requestDto = new EmailAuthKeyCheckRequestDto();
        requestDto.setEmail("notfound@teamgoo.com");
        requestDto.setAuthKey("123456");
        em.flush();
        em.clear();

        // expected
        assertThatThrownBy(() -> emailAuthService.checkEmailAuthKey(requestDto))
                .isInstanceOf(EmailAuthNotFoundException.class);
    }

    @Test
    @DisplayName("이메일 인증번호 확인 -> 인증번호 불일치 - 성공")
    void checkEmailAuthKey_IncorrectKey_Success() {
        // given
        emailAuthRepository.save(EmailAuth.builder()
                .email("test@teamgoo.com")
                .authKey("123456")
                .build());
        EmailAuthKeyCheckRequestDto requestDto = new EmailAuthKeyCheckRequestDto();
        requestDto.setEmail("test@teamgoo.com");
        requestDto.setAuthKey("000000");
        em.flush();
        em.clear();

        // when
        EmailAuthKeyCheckResponseDto responseDto = emailAuthService.checkEmailAuthKey(requestDto);

        // then
        assertThat(responseDto.getEmail()).isEqualTo("test@teamgoo.com");
        assertThat(responseDto.getAuthenticated()).isFalse();

        EmailAuth emailAuth = emailAuthRepository.findLatestByEmail("test@teamgoo.com")
                .orElseThrow(() -> new IllegalArgumentException("인증번호가 발급되지 않음."));
        assertThat(emailAuth.getEmail()).isEqualTo("test@teamgoo.com");
        assertThat(emailAuth.getAuthKey()).isEqualTo("123456");
        assertThat(emailAuth.getAuthenticated()).isFalse();
    }
}