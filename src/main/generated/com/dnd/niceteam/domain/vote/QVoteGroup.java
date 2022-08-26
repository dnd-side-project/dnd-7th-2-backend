package com.dnd.niceteam.domain.vote;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QVoteGroup is a Querydsl query type for VoteGroup
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QVoteGroup extends EntityPathBase<VoteGroup> {

    private static final long serialVersionUID = -95472344L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QVoteGroup voteGroup = new QVoteGroup("voteGroup");

    public final com.dnd.niceteam.domain.common.QBaseEntity _super = new com.dnd.niceteam.domain.common.QBaseEntity(this);

    public final BooleanPath complete = createBoolean("complete");

    //inherited
    public final StringPath createdBy = _super.createdBy;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final StringPath lastModifiedBy = _super.lastModifiedBy;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastModifiedDate = _super.lastModifiedDate;

    public final com.dnd.niceteam.domain.project.QProject project;

    //inherited
    public final BooleanPath useYn = _super.useYn;

    public final ListPath<Vote, QVote> votes = this.<Vote, QVote>createList("votes", Vote.class, QVote.class, PathInits.DIRECT2);

    public QVoteGroup(String variable) {
        this(VoteGroup.class, forVariable(variable), INITS);
    }

    public QVoteGroup(Path<? extends VoteGroup> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QVoteGroup(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QVoteGroup(PathMetadata metadata, PathInits inits) {
        this(VoteGroup.class, metadata, inits);
    }

    public QVoteGroup(Class<? extends VoteGroup> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.project = inits.isInitialized("project") ? new com.dnd.niceteam.domain.project.QProject(forProperty("project")) : null;
    }

}

