package com.dnd.niceteam.domain.project;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QSideProject is a Querydsl query type for SideProject
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QSideProject extends EntityPathBase<SideProject> {

    private static final long serialVersionUID = -1402879210L;

    public static final QSideProject sideProject = new QSideProject("sideProject");

    public final QProject _super = new QProject(this);

    //inherited
    public final StringPath createdBy = _super.createdBy;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    //inherited
    public final DatePath<java.time.LocalDate> endDate = _super.endDate;

    public final EnumPath<com.dnd.niceteam.domain.code.Field> field = createEnum("field", com.dnd.niceteam.domain.code.Field.class);

    public final EnumPath<com.dnd.niceteam.domain.code.FieldCategory> fieldCategory = createEnum("fieldCategory", com.dnd.niceteam.domain.code.FieldCategory.class);

    //inherited
    public final NumberPath<Long> id = _super.id;

    //inherited
    public final StringPath lastModifiedBy = _super.lastModifiedBy;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastModifiedDate = _super.lastModifiedDate;

    //inherited
    public final NumberPath<Integer> memberCount = _super.memberCount;

    //inherited
    public final StringPath name = _super.name;

    //inherited
    public final SetPath<ProjectMember, QProjectMember> projectMembers = _super.projectMembers;

    //inherited
    public final DatePath<java.time.LocalDate> startDate = _super.startDate;

    //inherited
    public final EnumPath<ProjectStatus> status = _super.status;

    //inherited
    public final EnumPath<com.dnd.niceteam.domain.code.Type> type = _super.type;

    //inherited
    public final BooleanPath useYn = _super.useYn;

    public QSideProject(String variable) {
        super(SideProject.class, forVariable(variable));
    }

    public QSideProject(Path<? extends SideProject> path) {
        super(path.getType(), path.getMetadata());
    }

    public QSideProject(PathMetadata metadata) {
        super(SideProject.class, metadata);
    }

}

