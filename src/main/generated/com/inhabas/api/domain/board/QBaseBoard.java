package com.inhabas.api.domain.board;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QBaseBoard is a Querydsl query type for BaseBoard
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBaseBoard extends EntityPathBase<BaseBoard> {

    private static final long serialVersionUID = -1072730980L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QBaseBoard baseBoard = new QBaseBoard("baseBoard");

    public final com.inhabas.api.domain.QBaseEntity _super = new com.inhabas.api.domain.QBaseEntity(this);

    public final ListPath<com.inhabas.api.domain.comment.Comment, com.inhabas.api.domain.comment.QComment> comments = this.<com.inhabas.api.domain.comment.Comment, com.inhabas.api.domain.comment.QComment>createList("comments", com.inhabas.api.domain.comment.Comment.class, com.inhabas.api.domain.comment.QComment.class, PathInits.DIRECT2);

    public final com.inhabas.api.domain.board.type.wrapper.QContents contents;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> created = _super.created;

    public final SetPath<com.inhabas.api.domain.file.BoardFile, com.inhabas.api.domain.file.QBoardFile> files = this.<com.inhabas.api.domain.file.BoardFile, com.inhabas.api.domain.file.QBoardFile>createSet("files", com.inhabas.api.domain.file.BoardFile.class, com.inhabas.api.domain.file.QBoardFile.class, PathInits.DIRECT2);

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final com.inhabas.api.domain.board.type.wrapper.QTitle title;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updated = _super.updated;

    public final com.inhabas.api.domain.member.QMember writer;

    public QBaseBoard(String variable) {
        this(BaseBoard.class, forVariable(variable), INITS);
    }

    public QBaseBoard(Path<? extends BaseBoard> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QBaseBoard(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QBaseBoard(PathMetadata metadata, PathInits inits) {
        this(BaseBoard.class, metadata, inits);
    }

    public QBaseBoard(Class<? extends BaseBoard> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.contents = inits.isInitialized("contents") ? new com.inhabas.api.domain.board.type.wrapper.QContents(forProperty("contents")) : null;
        this.title = inits.isInitialized("title") ? new com.inhabas.api.domain.board.type.wrapper.QTitle(forProperty("title")) : null;
        this.writer = inits.isInitialized("writer") ? new com.inhabas.api.domain.member.QMember(forProperty("writer"), inits.get("writer")) : null;
    }

}

