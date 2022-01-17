package com.inhabas.api.domain.member;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMember is a Querydsl query type for Member
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMember extends EntityPathBase<Member> {

    private static final long serialVersionUID = -1034965123L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMember member = new QMember("member1");

    public final QIbasInformation ibasInformation;

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final com.inhabas.api.domain.member.type.wrapper.QName name;

    public final com.inhabas.api.domain.member.type.wrapper.QPhone phone;

    public final StringPath picture = createString("picture");

    public final QSchoolInformation schoolInformation;

    public QMember(String variable) {
        this(Member.class, forVariable(variable), INITS);
    }

    public QMember(Path<? extends Member> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMember(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMember(PathMetadata metadata, PathInits inits) {
        this(Member.class, metadata, inits);
    }

    public QMember(Class<? extends Member> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.ibasInformation = inits.isInitialized("ibasInformation") ? new QIbasInformation(forProperty("ibasInformation"), inits.get("ibasInformation")) : null;
        this.name = inits.isInitialized("name") ? new com.inhabas.api.domain.member.type.wrapper.QName(forProperty("name")) : null;
        this.phone = inits.isInitialized("phone") ? new com.inhabas.api.domain.member.type.wrapper.QPhone(forProperty("phone")) : null;
        this.schoolInformation = inits.isInitialized("schoolInformation") ? new QSchoolInformation(forProperty("schoolInformation"), inits.get("schoolInformation")) : null;
    }

}

