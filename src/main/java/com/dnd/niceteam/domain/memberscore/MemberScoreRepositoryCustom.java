package com.dnd.niceteam.domain.memberscore;

import com.dnd.niceteam.domain.member.Member;

import java.util.Optional;

public interface MemberScoreRepositoryCustom {

    Optional<MemberScore> findByMember(Member member);
}
