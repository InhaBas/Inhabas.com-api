package com.inhabas.api.domain.comment;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QComment is a Querydsl query type for Comment
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QComment extends EntityPathBase<Comment> {

    private static final long serialVersionUID = 882644191L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QComment comment = new QComment("comment");

    public final com.inhabas.api.domain.QBaseEntity _super = new com.inhabas.api.domain.QBaseEntity(this);

    public final ListPath<Comment, QComment> children = this.<Comment, QComment>createList("children", Comment.class, QComment.class, PathInits.DIRECT2);

    public final com.inhabas.api.domain.comment.type.wrapper.QContents contents;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> created = _super.created;

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final com.inhabas.api.domain.board.QBaseBoard parentBoard;

    public final QComment parentComment;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updated = _super.updated;

    public final com.inhabas.api.domain.member.QMember writer;

    public QComment(String variable) {
        this(Comment.class, forVariable(variable), INITS);
    }

    public QComment(Path<? extends Comment> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QComment(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QComment(PathMetadata metadata, PathInits inits) {
        this(Comment.class, metadata, inits);
    }

    public QComment(Class<? extends Comment> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.contents = inits.isInitialized("contents") ? new com.inhabas.api.domain.comment.type.wrapper.QContents(forProperty("contents")) : null;
        this.parentBoard = inits.isInitialized("parentBoard") ? new com.inhabas.api.domain.board.QBaseBoard(forProperty("parentBoard"), inits.get("parentBoard")) : null;
        this.parentComment = inits.isInitialized("parentComment") ? new QComment(forProperty("parentComment"), inits.get("parentComment")) : null;
        this.writer = inits.isInitialized("writer") ? new com.inhabas.api.domain.member.QMember(forProperty("writer"), inits.get("writer")) : null;
    }

}

