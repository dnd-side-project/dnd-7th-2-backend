package com.dnd.niceteam.domain.emailauth;

import com.dnd.niceteam.common.TestJpaConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Transactional
@Import(TestJpaConfig.class)
class EmailAuthRepositoryTest {

    @Autowired
    private EmailAuthRepository emailAuthRepository;

    @Autowired
    private EntityManager em;

    @Test
    void findLatestByEmail() {
        // given
        emailAuthRepository.save(EmailAuth.builder()
                .email("test@teamgoo.com")
                .authKey("123456")
                .build());
        emailAuthRepository.save(EmailAuth.builder()
                .email("test@teamgoo.com")
                .authKey("234567")
                .build());
        emailAuthRepository.save(EmailAuth.builder()
                .email("test@teamgoo.com")
                .authKey("345678")
                .build());
        em.flush();
        em.clear();

        // when
        EmailAuth emailAuth = emailAuthRepository.findLatestByEmail("test@teamgoo.com")
                .orElseThrow(() -> new IllegalArgumentException("인증번호가 발급되지 않음."));
        assertThat(emailAuth.getAuthKey()).isEqualTo("345678");
    }
}