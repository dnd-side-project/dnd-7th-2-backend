package com.dnd.niceteam.member.util;

import com.dnd.niceteam.domain.member.Member;
import com.dnd.niceteam.domain.member.MemberRepository;
import com.dnd.niceteam.domain.member.MemberRepositoryCustom;
import com.dnd.niceteam.domain.member.exception.MemberNotFoundException;

public abstract class MemberUtils {

    public static Member findMemberByEmail(String email, MemberRepositoryCustom memberRepositoryCustom) {
        return memberRepositoryCustom.findByEmail(email)
                .orElseThrow(() -> new MemberNotFoundException("email = " + email));
    }

    public static Member findMemberById(Long id, MemberRepository memberRepository) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new MemberNotFoundException("id = " + id));
    }

}
