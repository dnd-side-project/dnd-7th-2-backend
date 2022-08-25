package com.dnd.niceteam.domain.university;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QUniversity is a Querydsl query type for University
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUniversity extends EntityPathBase<University> {

    private static final long serialVersionUID = -1126254441L;

    public static final QUniversity university = new QUniversity("university");

    public final StringPath emailDomain = createString("emailDomain");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath name = createString("name");

    public QUniversity(String variable) {
        super(University.class, forVariable(variable));
    }

    public QUniversity(Path<? extends University> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUniversity(PathMetadata metadata) {
        super(University.class, metadata);
    }

}

