package com.dnd.niceteam.domain.code;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QPersonality is a Querydsl query type for Personality
 */
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QPersonality extends BeanPath<Personality> {

    private static final long serialVersionUID = -2121275484L;

    public static final QPersonality personality = new QPersonality("personality");

    public final EnumPath<Personality.Adjective> adjective = createEnum("adjective", Personality.Adjective.class);

    public final EnumPath<Personality.Noun> noun = createEnum("noun", Personality.Noun.class);

    public QPersonality(String variable) {
        super(Personality.class, forVariable(variable));
    }

    public QPersonality(Path<? extends Personality> path) {
        super(path.getType(), path.getMetadata());
    }

    public QPersonality(PathMetadata metadata) {
        super(Personality.class, metadata);
    }

}

