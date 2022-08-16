package com.dnd.niceteam.domain.bookmark;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

import static com.dnd.niceteam.domain.account.QAccount.account;
import static com.dnd.niceteam.domain.bookmark.QBookmark.bookmark;
import static com.dnd.niceteam.domain.member.QMember.member;

@RequiredArgsConstructor
public class BookmarkRepositoryCustomImpl implements BookmarkRepositoryCustom {

    private final JPAQueryFactory query;

    @Override
    public boolean existsByIdAndEmail(long bookmarkId, String email) {
        return Optional.ofNullable(query
                        .selectOne()
                        .from(bookmark)
                        .join(bookmark.member, member)
                        .join(member.account, account)
                        .where(bookmark.id.eq(bookmarkId), account.email.eq(email))
                        .fetchFirst())
                .isPresent();
    }
}
