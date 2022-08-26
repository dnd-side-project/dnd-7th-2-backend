package com.dnd.niceteam.domain.project;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QLectureProject is a Querydsl query type for LectureProject
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QLectureProject extends EntityPathBase<LectureProject> {

    private static final long serialVersionUID = 109723111L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QLectureProject lectureProject = new QLectureProject("lectureProject");

    public final QProject _super = new QProject(this);

    //inherited
    public final StringPath createdBy = _super.createdBy;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final com.dnd.niceteam.domain.department.QDepartment department;

    //inherited
    public final DatePath<java.time.LocalDate> endDate = _super.endDate;

    //inherited
    public final NumberPath<Long> id = _super.id;

    //inherited
    public final StringPath lastModifiedBy = _super.lastModifiedBy;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastModifiedDate = _super.lastModifiedDate;

    public final SetPath<LectureTime, QLectureTime> lectureTimes = this.<LectureTime, QLectureTime>createSet("lectureTimes", LectureTime.class, QLectureTime.class, PathInits.DIRECT2);

    //inherited
    public final NumberPath<Integer> memberCount = _super.memberCount;

    //inherited
    public final StringPath name = _super.name;

    public final StringPath professor = createString("professor");

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

    public QLectureProject(String variable) {
        this(LectureProject.class, forVariable(variable), INITS);
    }

    public QLectureProject(Path<? extends LectureProject> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QLectureProject(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QLectureProject(PathMetadata metadata, PathInits inits) {
        this(LectureProject.class, metadata, inits);
    }

    public QLectureProject(Class<? extends LectureProject> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.department = inits.isInitialized("department") ? new com.dnd.niceteam.domain.department.QDepartment(forProperty("department"), inits.get("department")) : null;
    }

}

