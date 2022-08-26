package com.dnd.niceteam.domain.comment;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import java.util.Optional;

import static com.dnd.niceteam.domain.comment.QComment.comment;

@RequiredArgsConstructor
public class CommentRepositoryCustomImpl implements CommentRepositoryCustom {
    private final JPAQueryFactory query;

    @Override
    public Optional<Long> findMaxGroupNo() {
        return Optional.ofNullable(query
                .select(comment.groupNo.max())
                .from(comment)
                .fetchOne());
    }
}
