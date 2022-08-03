package com.dnd.niceteam.domain.emailauth;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

import static com.dnd.niceteam.domain.emailauth.QEmailAuth.*;

@RequiredArgsConstructor
public class EmailAuthRepositoryCustomImpl implements EmailAuthRepositoryCustom {

    private final JPAQueryFactory query;

    @Override
    public Optional<EmailAuth> findLatestByEmail(String email) {
        return Optional.ofNullable(query
                .selectFrom(emailAuth)
                .where(emailAuth.email.eq(email))
                .orderBy(emailAuth.createdDate.desc())
                .fetchFirst());
    }
}
