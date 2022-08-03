package com.dnd.niceteam.member.service;

import com.dnd.niceteam.domain.account.AccountRepository;
import com.dnd.niceteam.domain.emailauth.EmailAuthRepository;
import com.dnd.niceteam.domain.member.MemberRepository;
import com.dnd.niceteam.member.dto.DupCheck;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;

    private final AccountRepository accountRepository;

    private final EmailAuthRepository emailAuthRepository;

    public DupCheck.ResponseDto checkEmailDuplicate(String email) {
        DupCheck.ResponseDto responseDto = new DupCheck.ResponseDto();
        responseDto.setDuplicated(accountRepository.existsByEmail(email));
        return responseDto;
    }

    public DupCheck.ResponseDto checkNicknameDuplicate(String nickname) {
        DupCheck.ResponseDto responseDto = new DupCheck.ResponseDto();
        responseDto.setDuplicated(memberRepository.existsByNickname(nickname));
        return responseDto;
    }
}