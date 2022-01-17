package com.inhabas.api.domain.file.type.wrapper;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QFilePath is a Querydsl query type for FilePath
 */
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QFilePath extends BeanPath<FilePath> {

    private static final long serialVersionUID = -1583743601L;

    public static final QFilePath filePath = new QFilePath("filePath");

    public final StringPath value = createString("value");

    public QFilePath(String variable) {
        super(FilePath.class, forVariable(variable));
    }

    public QFilePath(Path<? extends FilePath> path) {
        super(path.getType(), path.getMetadata());
    }

    public QFilePath(PathMetadata metadata) {
        super(FilePath.class, metadata);
    }

}

