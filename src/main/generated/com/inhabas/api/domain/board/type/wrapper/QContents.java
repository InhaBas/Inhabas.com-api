package com.inhabas.api.domain.board.type.wrapper;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QContents is a Querydsl query type for Contents
 */
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QContents extends BeanPath<Contents> {

    private static final long serialVersionUID = -730543600L;

    public static final QContents contents = new QContents("contents");

    public final StringPath value = createString("value");

    public QContents(String variable) {
        super(Contents.class, forVariable(variable));
    }

    public QContents(Path<? extends Contents> path) {
        super(path.getType(), path.getMetadata());
    }

    public QContents(PathMetadata metadata) {
        super(Contents.class, metadata);
    }

}

