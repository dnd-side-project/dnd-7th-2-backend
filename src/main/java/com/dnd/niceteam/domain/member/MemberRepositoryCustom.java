package com.dnd.niceteam.domain.member;

import java.util.Optional;

public interface MemberRepositoryCustom {

    Optional<Member> findByEmail(String email);
}
