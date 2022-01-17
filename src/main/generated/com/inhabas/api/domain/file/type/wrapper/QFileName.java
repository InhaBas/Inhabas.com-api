package com.inhabas.api.domain.file.type.wrapper;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QFileName is a Querydsl query type for FileName
 */
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QFileName extends BeanPath<FileName> {

    private static final long serialVersionUID = -1583803403L;

    public static final QFileName fileName = new QFileName("fileName");

    public final StringPath value = createString("value");

    public QFileName(String variable) {
        super(FileName.class, forVariable(variable));
    }

    public QFileName(Path<? extends FileName> path) {
        super(path.getType(), path.getMetadata());
    }

    public QFileName(PathMetadata metadata) {
        super(FileName.class, metadata);
    }

}

