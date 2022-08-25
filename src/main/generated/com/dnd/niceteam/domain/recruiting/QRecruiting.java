package com.dnd.niceteam.domain.recruiting;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QRecruiting is a Querydsl query type for Recruiting
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QRecruiting extends EntityPathBase<Recruiting> {

    private static final long serialVersionUID = -1483659561L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QRecruiting recruiting = new QRecruiting("recruiting");

    public final com.dnd.niceteam.domain.common.QBaseEntity _super = new com.dnd.niceteam.domain.common.QBaseEntity(this);

    public final EnumPath<com.dnd.niceteam.domain.code.ActivityArea> activityArea = createEnum("activityArea", com.dnd.niceteam.domain.code.ActivityArea.class);

    public final SetPath<ActivityDayTime, QActivityDayTime> activityDayTimes = this.<ActivityDayTime, QActivityDayTime>createSet("activityDayTimes", ActivityDayTime.class, QActivityDayTime.class, PathInits.DIRECT2);

    public final NumberPath<Integer> bookmarkCount = createNumber("bookmarkCount", Integer.class);

    public final NumberPath<Integer> commentCount = createNumber("commentCount", Integer.class);

    public final StringPath content = createString("content");

    //inherited
    public final StringPath createdBy = _super.createdBy;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath introLink = createString("introLink");

    //inherited
    public final StringPath lastModifiedBy = _super.lastModifiedBy;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastModifiedDate = _super.lastModifiedDate;

    public final com.dnd.niceteam.domain.member.QMember member;

    public final SetPath<com.dnd.niceteam.domain.code.Personality.Adjective, EnumPath<com.dnd.niceteam.domain.code.Personality.Adjective>> personalityAdjectives = this.<com.dnd.niceteam.domain.code.Personality.Adjective, EnumPath<com.dnd.niceteam.domain.code.Personality.Adjective>>createSet("personalityAdjectives", com.dnd.niceteam.domain.code.Personality.Adjective.class, EnumPath.class, PathInits.DIRECT2);

    public final SetPath<com.dnd.niceteam.domain.code.Personality.Noun, EnumPath<com.dnd.niceteam.domain.code.Personality.Noun>> personalityNouns = this.<com.dnd.niceteam.domain.code.Personality.Noun, EnumPath<com.dnd.niceteam.domain.code.Personality.Noun>>createSet("personalityNouns", com.dnd.niceteam.domain.code.Personality.Noun.class, EnumPath.class, PathInits.DIRECT2);

    public final NumberPath<Integer> poolUpCount = createNumber("poolUpCount", Integer.class);

    public final DateTimePath<java.time.LocalDateTime> poolUpDate = createDateTime("poolUpDate", java.time.LocalDateTime.class);

    public final com.dnd.niceteam.domain.project.QProject project;

    public final DatePath<java.time.LocalDate> recruitingEndDate = createDate("recruitingEndDate", java.time.LocalDate.class);

    public final NumberPath<Integer> recruitingMemberCount = createNumber("recruitingMemberCount", Integer.class);

    public final EnumPath<com.dnd.niceteam.domain.code.Type> recruitingType = createEnum("recruitingType", com.dnd.niceteam.domain.code.Type.class);

    public final EnumPath<com.dnd.niceteam.domain.code.ProgressStatus> status = createEnum("status", com.dnd.niceteam.domain.code.ProgressStatus.class);

    public final StringPath title = createString("title");

    //inherited
    public final BooleanPath useYn = _super.useYn;

    public QRecruiting(String variable) {
        this(Recruiting.class, forVariable(variable), INITS);
    }

    public QRecruiting(Path<? extends Recruiting> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QRecruiting(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QRecruiting(PathMetadata metadata, PathInits inits) {
        this(Recruiting.class, metadata, inits);
    }

    public QRecruiting(Class<? extends Recruiting> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new com.dnd.niceteam.domain.member.QMember(forProperty("member"), inits.get("member")) : null;
        this.project = inits.isInitialized("project") ? new com.dnd.niceteam.domain.project.QProject(forProperty("project")) : null;
    }

}

