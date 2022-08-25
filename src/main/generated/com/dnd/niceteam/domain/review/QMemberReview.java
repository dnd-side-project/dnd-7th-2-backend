package com.dnd.niceteam.domain.review;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMemberReview is a Querydsl query type for MemberReview
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMemberReview extends EntityPathBase<MemberReview> {

    private static final long serialVersionUID = -343467439L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMemberReview memberReview = new QMemberReview("memberReview");

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

    public final SetPath<MemberReviewTag, QMemberReviewTag> memberReviewTags = this.<MemberReviewTag, QMemberReviewTag>createSet("memberReviewTags", MemberReviewTag.class, QMemberReviewTag.class, PathInits.DIRECT2);

    public final NumberPath<Integer> participationScore = createNumber("participationScore", Integer.class);

    public final com.dnd.niceteam.domain.project.QProjectMember reviewee;

    public final com.dnd.niceteam.domain.project.QProjectMember reviewer;

    public final BooleanPath skipped = createBoolean("skipped");

    public final NumberPath<Integer> teamAgainScore = createNumber("teamAgainScore", Integer.class);

    //inherited
    public final BooleanPath useYn = _super.useYn;

    public QMemberReview(String variable) {
        this(MemberReview.class, forVariable(variable), INITS);
    }

    public QMemberReview(Path<? extends MemberReview> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMemberReview(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMemberReview(PathMetadata metadata, PathInits inits) {
        this(MemberReview.class, metadata, inits);
    }

    public QMemberReview(Class<? extends MemberReview> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.reviewee = inits.isInitialized("reviewee") ? new com.dnd.niceteam.domain.project.QProjectMember(forProperty("reviewee"), inits.get("reviewee")) : null;
        this.reviewer = inits.isInitialized("reviewer") ? new com.dnd.niceteam.domain.project.QProjectMember(forProperty("reviewer"), inits.get("reviewer")) : null;
    }

}

