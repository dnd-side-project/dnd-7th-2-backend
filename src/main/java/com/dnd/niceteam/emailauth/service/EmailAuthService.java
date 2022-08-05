package com.dnd.niceteam.emailauth.service;

import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.model.*;
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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EmailAuthService {

    private static final String EMAIL_AUTH_SUBJECT = "[팀구] 회원가입 이메일 인증";

    private final AmazonSimpleEmailService emailService;

    private final EmailAuthRepository emailAuthRepository;

    private final UniversityRepository universityRepository;

    private final EmailAuthKeyCreator keyCreator;

    @Transactional
    public void sendEmailAuthKey(EmailAuthKeySendRequestDto requestDto) {
        University university = universityRepository.findByName(requestDto.getUnivName())
                .orElseThrow(() -> new UniversityNotFoundException("University name = " + requestDto.getUnivName()));
        String email = requestDto.getEmail();
        if (isInvalidEmailDomain(university, email)) {
            throw new InvalidEmailDomainException("email = " + email);
        }
        EmailAuth emailAuth = emailAuthRepository.save(EmailAuth.builder()
                .email(email)
                .authKey(keyCreator.createEmailAuthKey())
                .build());
        SendEmailRequest sendEmailRequest = createSendEmailRequest(emailAuth);
        emailService.sendEmail(sendEmailRequest);
    }

    private SendEmailRequest createSendEmailRequest(EmailAuth emailAuth) {
        Content content = new Content()
                .withData("인증번호: " + emailAuth.getAuthKey())
                .withCharset(StandardCharsets.UTF_8.name());
        Message message = new Message()
                .withSubject(new Content(EMAIL_AUTH_SUBJECT).withCharset(StandardCharsets.UTF_8.name()))
                .withBody(new Body(content));
        return new SendEmailRequest()
                .withDestination(new Destination(List.of(emailAuth.getEmail())))
                .withSource("nice2meetteam@gmail.com")
                .withMessage(message);
    }

    private boolean isInvalidEmailDomain(University university, String email) {
        return !email.endsWith(university.getEmailDomain());
    }

    @Transactional
    public EmailAuthKeyCheckResponseDto checkEmailAuthKey(EmailAuthKeyCheckRequestDto requestDto) {
        String email = requestDto.getEmail();
        EmailAuth emailAuth = emailAuthRepository.findLatestByEmail(email)
                .orElseThrow(() -> new EmailAuthNotFoundException("email = " + email));
        if (isValidKey(emailAuth, requestDto.getAuthKey())) {
            emailAuth.authenticate();
        }

        EmailAuthKeyCheckResponseDto responseDto = new EmailAuthKeyCheckResponseDto();
        responseDto.setEmail(email);
        responseDto.setAuthenticated(emailAuth.getAuthenticated());
        return responseDto;
    }

    private boolean isValidKey(EmailAuth emailAuth, String authKey) {
        return emailAuth.getAuthKey().equals(authKey) &&
                LocalDateTime.now().isBefore(emailAuth.getCreatedDate().plusMinutes(5L));
    }
}
