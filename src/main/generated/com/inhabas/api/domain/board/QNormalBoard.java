package com.inhabas.api.domain.board;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QNormalBoard is a Querydsl query type for NormalBoard
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QNormalBoard extends EntityPathBase<NormalBoard> {

    private static final long serialVersionUID = 1551150726L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QNormalBoard normalBoard = new QNormalBoard("normalBoard");

    public final QBaseBoard _super;

    public final EnumPath<Category> category = createEnum("category", Category.class);

    //inherited
    public final ListPath<com.inhabas.api.domain.comment.Comment, com.inhabas.api.domain.comment.QComment> comments;

    // inherited
    public final com.inhabas.api.domain.board.type.wrapper.QContents contents;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> created;

    //inherited
    public final SetPath<com.inhabas.api.domain.file.BoardFile, com.inhabas.api.domain.file.QBoardFile> files;

    //inherited
    public final NumberPath<Integer> id;

    // inherited
    public final com.inhabas.api.domain.board.type.wrapper.QTitle title;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updated;

    // inherited
    public final com.inhabas.api.domain.member.QMember writer;

    public QNormalBoard(String variable) {
        this(NormalBoard.class, forVariable(variable), INITS);
    }

    public QNormalBoard(Path<? extends NormalBoard> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QNormalBoard(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QNormalBoard(PathMetadata metadata, PathInits inits) {
        this(NormalBoard.class, metadata, inits);
    }

    public QNormalBoard(Class<? extends NormalBoard> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this._super = new QBaseBoard(type, metadata, inits);
        this.comments = _super.comments;
        this.contents = _super.contents;
        this.created = _super.created;
        this.files = _super.files;
        this.id = _super.id;
        this.title = _super.title;
        this.updated = _super.updated;
        this.writer = _super.writer;
    }

}

