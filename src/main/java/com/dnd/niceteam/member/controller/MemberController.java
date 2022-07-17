package com.dnd.niceteam.member.controller;

import com.dnd.niceteam.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RequiredArgsConstructor
@RequestMapping("/member")
@RestController
public class MemberController {
    private final MemberService memberService;
}