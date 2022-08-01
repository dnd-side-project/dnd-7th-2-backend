package com.dnd.niceteam.emailauth.service;

import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.dnd.niceteam.common.TestJpaConfig;
import com.dnd.niceteam.domain.emailauth.EmailAuth;
import com.dnd.niceteam.domain.emailauth.EmailAuthRepository;
import com.dnd.niceteam.domain.university.University;
import com.dnd.niceteam.domain.university.UniversityRepository;
import com.dnd.niceteam.domain.university.exception.InvalidEmailDomainException;
import com.dnd.niceteam.domain.university.exception.UniversityNotFoundException;
import com.dnd.niceteam.emailauth.dto.EmailAuthKeySendRequestDto;
import org.junit.jupiter.api.BeforeEach;
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
    void sendEmailAuthKey_Success() {
        // given
        universityRepository.save(University.builder()
                .name("팀구대학교")
                .emailDomain("teamgoo.com")
                .build());
        EmailAuthKeySendRequestDto requestDto = new EmailAuthKeySendRequestDto();
        requestDto.setEmail("test@teamgoo.com");
        requestDto.setUnivName("팀구대학교");
        given(mockKeyCreator.createEmailAuthKey()).willReturn("123456");

        em.flush();
        em.clear();

        // when
        emailAuthService.sendEmailAuthKey(requestDto);

        // then
        EmailAuth emailAuth = emailAuthRepository.findByEmail("test@teamgoo.com")
                .orElseThrow(() -> new IllegalArgumentException("Email not found."));
        assertThat(emailAuth.getAuthKey()).isEqualTo("123456");
    }

    @Test
    void sendEmailAuthKey_NotFoundUniv_ThenFail() {
        // given
        universityRepository.save(University.builder()
                .name("팀구대학교")
                .emailDomain("teamgoo.com")
                .build());
        EmailAuthKeySendRequestDto requestDto = new EmailAuthKeySendRequestDto();
        requestDto.setEmail("test@teamgoo.com");
        requestDto.setUnivName("없는대학교");
        given(mockKeyCreator.createEmailAuthKey()).willReturn("123456");

        em.flush();
        em.clear();

        // expected
        assertThatThrownBy(() -> emailAuthService.sendEmailAuthKey(requestDto))
                .isInstanceOf(UniversityNotFoundException.class);
    }

    @Test
    void sendEmailAuthKey_InvalidEmail_ThenFail() {
        // given
        universityRepository.save(University.builder()
                .name("팀구대학교")
                .emailDomain("teamgoo.com")
                .build());
        EmailAuthKeySendRequestDto requestDto = new EmailAuthKeySendRequestDto();
        requestDto.setEmail("test@invalidemail.com");
        requestDto.setUnivName("팀구대학교");
        given(mockKeyCreator.createEmailAuthKey()).willReturn("123456");

        em.flush();
        em.clear();

        // expected
        assertThatThrownBy(() -> emailAuthService.sendEmailAuthKey(requestDto))
                .isInstanceOf(InvalidEmailDomainException.class);
    }
}