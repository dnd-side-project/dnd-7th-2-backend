package com.dnd.niceteam.member.controller;

import com.dnd.niceteam.member.dto.MemberRequestDto;
import com.dnd.niceteam.member.dto.MemberResponseDto;
import com.dnd.niceteam.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RequiredArgsConstructor
@RequestMapping("/api/member")
@RestController
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody MemberRequestDto.Login loginRequestDto) {
        MemberResponseDto.TokenInfo login = memberService.login(loginRequestDto);

        // TODO: 2022-07-14 ApiResponseObject로 넘겨야함.
        return new ResponseEntity<>(login, HttpStatus.CREATED);
    }

    @PostMapping("/reissue")
    public ResponseEntity<?> reissue(@Valid @RequestBody MemberRequestDto.Reissue reissueRequestDto) {
        MemberResponseDto.Reissue reissue = memberService.reissueAccessToken(reissueRequestDto);

        // TODO: 2022-07-14 ApiResponseObject로 넘겨야함.
        return new ResponseEntity<>(reissue, HttpStatus.CREATED);
    }
}