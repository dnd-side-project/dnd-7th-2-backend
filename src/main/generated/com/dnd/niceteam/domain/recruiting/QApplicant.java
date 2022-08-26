package com.dnd.niceteam.domain.recruiting;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QApplicant is a Querydsl query type for Applicant
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QApplicant extends EntityPathBase<Applicant> {

    private static final long serialVersionUID = -1973310033L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QApplicant applicant = new QApplicant("applicant");

    public final com.dnd.niceteam.domain.common.QBaseTimeEntity _super = new com.dnd.niceteam.domain.common.QBaseTimeEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath joined = createBoolean("joined");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastModifiedDate = _super.lastModifiedDate;

    public final com.dnd.niceteam.domain.member.QMember member;

    public final QRecruiting recruiting;

    //inherited
    public final BooleanPath useYn = _super.useYn;

    public QApplicant(String variable) {
        this(Applicant.class, forVariable(variable), INITS);
    }

    public QApplicant(Path<? extends Applicant> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QApplicant(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QApplicant(PathMetadata metadata, PathInits inits) {
        this(Applicant.class, metadata, inits);
    }

    public QApplicant(Class<? extends Applicant> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new com.dnd.niceteam.domain.member.QMember(forProperty("member"), inits.get("member")) : null;
        this.recruiting = inits.isInitialized("recruiting") ? new QRecruiting(forProperty("recruiting"), inits.get("recruiting")) : null;
    }

}

