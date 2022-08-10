package com.dnd.niceteam.member.util;

import com.dnd.niceteam.domain.member.Member;
import com.dnd.niceteam.domain.member.MemberRepositoryCustom;
import com.dnd.niceteam.domain.member.exception.MemberNotFoundException;

public abstract class MemberUtils {

    public static Member findMemberByEmail(String email, MemberRepositoryCustom memberRepository) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new MemberNotFoundException("email = " + email));
    }

}
