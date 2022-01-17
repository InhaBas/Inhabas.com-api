package com.inhabas.api.domain.file;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QBaseFile is a Querydsl query type for BaseFile
 */
@Generated("com.querydsl.codegen.DefaultSupertypeSerializer")
public class QBaseFile extends EntityPathBase<BaseFile> {

    private static final long serialVersionUID = 422628142L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QBaseFile baseFile = new QBaseFile("baseFile");

    public final com.inhabas.api.domain.file.type.wrapper.QFileName filename;

    public final com.inhabas.api.domain.file.type.wrapper.QFilePath filepath;

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final com.inhabas.api.domain.file.type.wrapper.QFilePath legacyPath;

    public final DateTimePath<java.time.LocalDateTime> uploaded = createDateTime("uploaded", java.time.LocalDateTime.class);

    public QBaseFile(String variable) {
        this(BaseFile.class, forVariable(variable), INITS);
    }

    public QBaseFile(Path<? extends BaseFile> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QBaseFile(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QBaseFile(PathMetadata metadata, PathInits inits) {
        this(BaseFile.class, metadata, inits);
    }

    public QBaseFile(Class<? extends BaseFile> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.filename = inits.isInitialized("filename") ? new com.inhabas.api.domain.file.type.wrapper.QFileName(forProperty("filename")) : null;
        this.filepath = inits.isInitialized("filepath") ? new com.inhabas.api.domain.file.type.wrapper.QFilePath(forProperty("filepath")) : null;
        this.legacyPath = inits.isInitialized("legacyPath") ? new com.inhabas.api.domain.file.type.wrapper.QFilePath(forProperty("legacyPath")) : null;
    }

}

