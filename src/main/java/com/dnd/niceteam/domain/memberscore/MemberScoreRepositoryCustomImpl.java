package com.dnd.niceteam.domain.memberscore;

import com.dnd.niceteam.domain.member.Member;
import com.dnd.niceteam.domain.member.QMember;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

import static com.dnd.niceteam.domain.memberscore.QMemberScore.*;

@RequiredArgsConstructor
public class MemberScoreRepositoryCustomImpl implements MemberScoreRepositoryCustom {

    private final JPAQueryFactory query;

    @Override
    public Optional<MemberScore> findByMember(Member member) {
        QMember qMember = QMember.member;
        return Optional.ofNullable(query
                .select(memberScore)
                .from(qMember)
                .join(qMember.memberScore, memberScore)
                .where(qMember.eq(member))
                .fetchOne()
        );
    }
}
