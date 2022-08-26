package com.dnd.niceteam.domain.memberscore;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QMemberScore is a Querydsl query type for MemberScore
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMemberScore extends EntityPathBase<MemberScore> {

    private static final long serialVersionUID = -996989045L;

    public static final QMemberScore memberScore = new QMemberScore("memberScore");

    public final com.dnd.niceteam.domain.common.QBaseEntity _super = new com.dnd.niceteam.domain.common.QBaseEntity(this);

    //inherited
    public final StringPath createdBy = _super.createdBy;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final StringPath lastModifiedBy = _super.lastModifiedBy;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastModifiedDate = _super.lastModifiedDate;

    public final NumberPath<Integer> level = createNumber("level", Integer.class);

    public final NumberPath<Integer> reviewNum = createNumber("reviewNum", Integer.class);

    public final MapPath<com.dnd.niceteam.domain.code.ReviewTag, Integer, NumberPath<Integer>> reviewTagToNums = this.<com.dnd.niceteam.domain.code.ReviewTag, Integer, NumberPath<Integer>>createMap("reviewTagToNums", com.dnd.niceteam.domain.code.ReviewTag.class, Integer.class, NumberPath.class);

    public final NumberPath<Double> totalFeeds = createNumber("totalFeeds", Double.class);

    public final NumberPath<Integer> totalParticipationScore = createNumber("totalParticipationScore", Integer.class);

    public final NumberPath<Integer> totalTeamAgainScore = createNumber("totalTeamAgainScore", Integer.class);

    //inherited
    public final BooleanPath useYn = _super.useYn;

    public QMemberScore(String variable) {
        super(MemberScore.class, forVariable(variable));
    }

    public QMemberScore(Path<? extends MemberScore> path) {
        super(path.getType(), path.getMetadata());
    }

    public QMemberScore(PathMetadata metadata) {
        super(MemberScore.class, metadata);
    }

}

