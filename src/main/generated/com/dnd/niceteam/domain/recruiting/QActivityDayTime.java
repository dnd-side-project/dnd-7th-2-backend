package com.dnd.niceteam.domain.recruiting;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QActivityDayTime is a Querydsl query type for ActivityDayTime
 */
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QActivityDayTime extends BeanPath<ActivityDayTime> {

    private static final long serialVersionUID = -368764057L;

    public static final QActivityDayTime activityDayTime = new QActivityDayTime("activityDayTime");

    public final EnumPath<com.dnd.niceteam.domain.code.DayOfWeek> dayOfWeek = createEnum("dayOfWeek", com.dnd.niceteam.domain.code.DayOfWeek.class);

    public final TimePath<java.time.LocalTime> endTime = createTime("endTime", java.time.LocalTime.class);

    public final TimePath<java.time.LocalTime> startTime = createTime("startTime", java.time.LocalTime.class);

    public QActivityDayTime(String variable) {
        super(ActivityDayTime.class, forVariable(variable));
    }

    public QActivityDayTime(Path<? extends ActivityDayTime> path) {
        super(path.getType(), path.getMetadata());
    }

    public QActivityDayTime(PathMetadata metadata) {
        super(ActivityDayTime.class, metadata);
    }

}

