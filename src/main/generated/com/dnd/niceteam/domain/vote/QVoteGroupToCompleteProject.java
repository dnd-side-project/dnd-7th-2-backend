package com.dnd.niceteam.domain.vote;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QVoteGroupToCompleteProject is a Querydsl query type for VoteGroupToCompleteProject
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QVoteGroupToCompleteProject extends EntityPathBase<VoteGroupToCompleteProject> {

    private static final long serialVersionUID = 1247351645L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QVoteGroupToCompleteProject voteGroupToCompleteProject = new QVoteGroupToCompleteProject("voteGroupToCompleteProject");

    public final QVoteGroup _super;

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

    public final BooleanPath voteCompleted = createBoolean("voteCompleted");

    //inherited
    public final ListPath<Vote, QVote> votes;

    public QVoteGroupToCompleteProject(String variable) {
        this(VoteGroupToCompleteProject.class, forVariable(variable), INITS);
    }

    public QVoteGroupToCompleteProject(Path<? extends VoteGroupToCompleteProject> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QVoteGroupToCompleteProject(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QVoteGroupToCompleteProject(PathMetadata metadata, PathInits inits) {
        this(VoteGroupToCompleteProject.class, metadata, inits);
    }

    public QVoteGroupToCompleteProject(Class<? extends VoteGroupToCompleteProject> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this._super = new QVoteGroup(type, metadata, inits);
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

