package com.dnd.niceteam.domain.project;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QLectureTime is a Querydsl query type for LectureTime
 */
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QLectureTime extends BeanPath<LectureTime> {

    private static final long serialVersionUID = -275250497L;

    public static final QLectureTime lectureTime = new QLectureTime("lectureTime");

    public final EnumPath<com.dnd.niceteam.domain.code.DayOfWeek> dayOfWeek = createEnum("dayOfWeek", com.dnd.niceteam.domain.code.DayOfWeek.class);

    public final TimePath<java.time.LocalTime> startTime = createTime("startTime", java.time.LocalTime.class);

    public QLectureTime(String variable) {
        super(LectureTime.class, forVariable(variable));
    }

    public QLectureTime(Path<? extends LectureTime> path) {
        super(path.getType(), path.getMetadata());
    }

    public QLectureTime(PathMetadata metadata) {
        super(LectureTime.class, metadata);
    }

}

