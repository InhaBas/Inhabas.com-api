package com.inhabas.api.domain.member;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QIbasInformation is a Querydsl query type for IbasInformation
 */
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QIbasInformation extends BeanPath<IbasInformation> {

    private static final long serialVersionUID = 2083736478L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QIbasInformation ibasInformation = new QIbasInformation("ibasInformation");

    public final NumberPath<Integer> applyPublish = createNumber("applyPublish", Integer.class);

    public final com.inhabas.api.domain.member.type.wrapper.QIntroduce introduce;

    public final DateTimePath<java.time.LocalDateTime> joined = createDateTime("joined", java.time.LocalDateTime.class);

    public final EnumPath<com.inhabas.api.domain.member.type.wrapper.Role> role = createEnum("role", com.inhabas.api.domain.member.type.wrapper.Role.class);

    public QIbasInformation(String variable) {
        this(IbasInformation.class, forVariable(variable), INITS);
    }

    public QIbasInformation(Path<? extends IbasInformation> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QIbasInformation(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QIbasInformation(PathMetadata metadata, PathInits inits) {
        this(IbasInformation.class, metadata, inits);
    }

    public QIbasInformation(Class<? extends IbasInformation> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.introduce = inits.isInitialized("introduce") ? new com.inhabas.api.domain.member.type.wrapper.QIntroduce(forProperty("introduce")) : null;
    }

}

