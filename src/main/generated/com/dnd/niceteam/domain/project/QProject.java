package com.dnd.niceteam.domain.project;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QProject is a Querydsl query type for Project
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QProject extends EntityPathBase<Project> {

    private static final long serialVersionUID = 753349229L;

    public static final QProject project = new QProject("project");

    public final com.dnd.niceteam.domain.common.QBaseEntity _super = new com.dnd.niceteam.domain.common.QBaseEntity(this);

    //inherited
    public final StringPath createdBy = _super.createdBy;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final DatePath<java.time.LocalDate> endDate = createDate("endDate", java.time.LocalDate.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final StringPath lastModifiedBy = _super.lastModifiedBy;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastModifiedDate = _super.lastModifiedDate;

    public final NumberPath<Integer> memberCount = createNumber("memberCount", Integer.class);

    public final StringPath name = createString("name");

    public final SetPath<ProjectMember, QProjectMember> projectMembers = this.<ProjectMember, QProjectMember>createSet("projectMembers", ProjectMember.class, QProjectMember.class, PathInits.DIRECT2);

    public final DatePath<java.time.LocalDate> startDate = createDate("startDate", java.time.LocalDate.class);

    public final EnumPath<ProjectStatus> status = createEnum("status", ProjectStatus.class);

    public final EnumPath<com.dnd.niceteam.domain.code.Type> type = createEnum("type", com.dnd.niceteam.domain.code.Type.class);

    //inherited
    public final BooleanPath useYn = _super.useYn;

    public QProject(String variable) {
        super(Project.class, forVariable(variable));
    }

    public QProject(Path<? extends Project> path) {
        super(path.getType(), path.getMetadata());
    }

    public QProject(PathMetadata metadata) {
        super(Project.class, metadata);
    }

}

