package com.dnd.niceteam.domain.review;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMemberReviewTag is a Querydsl query type for MemberReviewTag
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMemberReviewTag extends EntityPathBase<MemberReviewTag> {

    private static final long serialVersionUID = -1626292343L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMemberReviewTag memberReviewTag = new QMemberReviewTag("memberReviewTag");

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

    public final QMemberReview memberReview;

    public final EnumPath<com.dnd.niceteam.domain.code.ReviewTag> tag = createEnum("tag", com.dnd.niceteam.domain.code.ReviewTag.class);

    //inherited
    public final BooleanPath useYn = _super.useYn;

    public QMemberReviewTag(String variable) {
        this(MemberReviewTag.class, forVariable(variable), INITS);
    }

    public QMemberReviewTag(Path<? extends MemberReviewTag> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMemberReviewTag(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMemberReviewTag(PathMetadata metadata, PathInits inits) {
        this(MemberReviewTag.class, metadata, inits);
    }

    public QMemberReviewTag(Class<? extends MemberReviewTag> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.memberReview = inits.isInitialized("memberReview") ? new QMemberReview(forProperty("memberReview"), inits.get("memberReview")) : null;
    }

}

