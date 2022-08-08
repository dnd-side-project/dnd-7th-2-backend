package com.dnd.niceteam.member.controller;

import com.dnd.niceteam.common.dto.ApiResult;
import com.dnd.niceteam.member.dto.DupCheck;
import com.dnd.niceteam.member.dto.MemberCreation;
import com.dnd.niceteam.member.dto.MemberUpdate;
import com.dnd.niceteam.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;


@RequiredArgsConstructor
@RequestMapping("/members")
@RestController
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/dup-check/email")
    public ResponseEntity<ApiResult<DupCheck.ResponseDto>> memberEmailDupCheck(@RequestParam @Email String email) {
        DupCheck.ResponseDto responseDto = memberService.checkEmailDuplicate(email);
        ApiResult<DupCheck.ResponseDto> apiResult = ApiResult.success(responseDto);
        return ResponseEntity.ok(apiResult);
    }

    @GetMapping("/dup-check/nickname")
    public ResponseEntity<ApiResult<DupCheck.ResponseDto>> memberNicknameDupCheck(
            @RequestParam @NotBlank @Size(max = 10) String nickname) {
        DupCheck.ResponseDto responseDto = memberService.checkNicknameDuplicate(nickname);
        ApiResult<DupCheck.ResponseDto> apiResult = ApiResult.success(responseDto);
        return ResponseEntity.ok(apiResult);
    }

    @PostMapping
    public ResponseEntity<ApiResult<MemberCreation.ResponseDto>> memberJoin(
            @RequestBody @Valid MemberCreation.RequestDto requestDto) {
        MemberCreation.ResponseDto responseDto = memberService.createMember(requestDto);
        ApiResult<MemberCreation.ResponseDto> apiResult = ApiResult.success(responseDto);
        return ResponseEntity.ok(apiResult);
    }

    @PutMapping
    public ResponseEntity<ApiResult<MemberUpdate.ResponseDto>> memberUpdate(
            @RequestBody @Valid MemberUpdate.RequestDto requestDto) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        MemberUpdate.ResponseDto responseDto = memberService.updateMember(username, requestDto);
        ApiResult<MemberUpdate.ResponseDto> apiResult = ApiResult.success(responseDto);
        return ResponseEntity.ok(apiResult);
    }
}