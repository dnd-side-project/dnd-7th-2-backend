package com.dnd.niceteam.domain.member;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMember is a Querydsl query type for Member
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMember extends EntityPathBase<Member> {

    private static final long serialVersionUID = 282471447L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMember member = new QMember("member1");

    public final com.dnd.niceteam.domain.common.QBaseEntity _super = new com.dnd.niceteam.domain.common.QBaseEntity(this);

    public final com.dnd.niceteam.domain.account.QAccount account;

    public final NumberPath<Integer> admissionYear = createNumber("admissionYear", Integer.class);

    //inherited
    public final StringPath createdBy = _super.createdBy;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final com.dnd.niceteam.domain.department.QDepartment department;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final SetPath<com.dnd.niceteam.domain.code.Field, EnumPath<com.dnd.niceteam.domain.code.Field>> interestingFields = this.<com.dnd.niceteam.domain.code.Field, EnumPath<com.dnd.niceteam.domain.code.Field>>createSet("interestingFields", com.dnd.niceteam.domain.code.Field.class, EnumPath.class, PathInits.DIRECT2);

    public final StringPath introduction = createString("introduction");

    public final StringPath introductionUrl = createString("introductionUrl");

    //inherited
    public final StringPath lastModifiedBy = _super.lastModifiedBy;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastModifiedDate = _super.lastModifiedDate;

    public final com.dnd.niceteam.domain.memberscore.QMemberScore memberScore;

    public final StringPath nickname = createString("nickname");

    public final com.dnd.niceteam.domain.code.QPersonality personality;

    public final com.dnd.niceteam.domain.university.QUniversity university;

    //inherited
    public final BooleanPath useYn = _super.useYn;

    public QMember(String variable) {
        this(Member.class, forVariable(variable), INITS);
    }

    public QMember(Path<? extends Member> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMember(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMember(PathMetadata metadata, PathInits inits) {
        this(Member.class, metadata, inits);
    }

    public QMember(Class<? extends Member> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.account = inits.isInitialized("account") ? new com.dnd.niceteam.domain.account.QAccount(forProperty("account")) : null;
        this.department = inits.isInitialized("department") ? new com.dnd.niceteam.domain.department.QDepartment(forProperty("department"), inits.get("department")) : null;
        this.memberScore = inits.isInitialized("memberScore") ? new com.dnd.niceteam.domain.memberscore.QMemberScore(forProperty("memberScore")) : null;
        this.personality = inits.isInitialized("personality") ? new com.dnd.niceteam.domain.code.QPersonality(forProperty("personality")) : null;
        this.university = inits.isInitialized("university") ? new com.dnd.niceteam.domain.university.QUniversity(forProperty("university")) : null;
    }

}

