package com.dnd.niceteam.domain.vote;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QVoteGroupToExpel is a Querydsl query type for VoteGroupToExpel
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QVoteGroupToExpel extends EntityPathBase<VoteGroupToExpel> {

    private static final long serialVersionUID = -1790697599L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QVoteGroupToExpel voteGroupToExpel = new QVoteGroupToExpel("voteGroupToExpel");

    public final QVoteGroup _super;

    public final com.dnd.niceteam.domain.project.QProjectMember candidate;

    //inherited
    public final BooleanPath complete;

    //inherited
    public final StringPath createdBy;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate;

    //inherited
    public final NumberPath<Long> id;

    //inherited
    public final StringPath lastModifiedBy;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastModifiedDate;

    // inherited
    public final com.dnd.niceteam.domain.project.QProject project;

    //inherited
    public final BooleanPath useYn;

    //inherited
    public final ListPath<Vote, QVote> votes;

    public QVoteGroupToExpel(String variable) {
        this(VoteGroupToExpel.class, forVariable(variable), INITS);
    }

    public QVoteGroupToExpel(Path<? extends VoteGroupToExpel> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QVoteGroupToExpel(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QVoteGroupToExpel(PathMetadata metadata, PathInits inits) {
        this(VoteGroupToExpel.class, metadata, inits);
    }

    public QVoteGroupToExpel(Class<? extends VoteGroupToExpel> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this._super = new QVoteGroup(type, metadata, inits);
        this.candidate = inits.isInitialized("candidate") ? new com.dnd.niceteam.domain.project.QProjectMember(forProperty("candidate"), inits.get("candidate")) : null;
        this.complete = _super.complete;
        this.createdBy = _super.createdBy;
        this.createdDate = _super.createdDate;
        this.id = _super.id;
        this.lastModifiedBy = _super.lastModifiedBy;
        this.lastModifiedDate = _super.lastModifiedDate;
        this.project = _super.project;
        this.useYn = _super.useYn;
        this.votes = _super.votes;
    }

}

