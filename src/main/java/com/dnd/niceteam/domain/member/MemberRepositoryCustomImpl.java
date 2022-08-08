package com.dnd.niceteam.domain.member;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

import static com.dnd.niceteam.domain.account.QAccount.account;
import static com.dnd.niceteam.domain.member.QMember.*;

@RequiredArgsConstructor
public class MemberRepositoryCustomImpl implements MemberRepositoryCustom {

    private final JPAQueryFactory query;

    @Override
    public Optional<Member> findByEmail(String email) {
        return Optional.ofNullable(query
                .selectFrom(member)
                .join(member.account, account)
                .where(account.email.eq(email))
                .fetchOne()
        );
    }
}
