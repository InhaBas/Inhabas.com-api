package com.inhabas.api.domain.file;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QBoardFile is a Querydsl query type for BoardFile
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBoardFile extends EntityPathBase<BoardFile> {

    private static final long serialVersionUID = 449753697L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QBoardFile boardFile = new QBoardFile("boardFile");

    public final QBaseFile _super;

    // inherited
    public final com.inhabas.api.domain.file.type.wrapper.QFileName filename;

    // inherited
    public final com.inhabas.api.domain.file.type.wrapper.QFilePath filepath;

    //inherited
    public final NumberPath<Integer> id;

    // inherited
    public final com.inhabas.api.domain.file.type.wrapper.QFilePath legacyPath;

    public final com.inhabas.api.domain.board.QBaseBoard parentBoard;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> uploaded;

    public QBoardFile(String variable) {
        this(BoardFile.class, forVariable(variable), INITS);
    }

    public QBoardFile(Path<? extends BoardFile> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QBoardFile(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QBoardFile(PathMetadata metadata, PathInits inits) {
        this(BoardFile.class, metadata, inits);
    }

    public QBoardFile(Class<? extends BoardFile> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this._super = new QBaseFile(type, metadata, inits);
        this.filename = _super.filename;
        this.filepath = _super.filepath;
        this.id = _super.id;
        this.legacyPath = _super.legacyPath;
        this.parentBoard = inits.isInitialized("parentBoard") ? new com.inhabas.api.domain.board.QBaseBoard(forProperty("parentBoard"), inits.get("parentBoard")) : null;
        this.uploaded = _super.uploaded;
    }

}

