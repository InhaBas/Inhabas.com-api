package com.inhabas.api.domain.member;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QSchoolInformation is a Querydsl query type for SchoolInformation
 */
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QSchoolInformation extends BeanPath<SchoolInformation> {

    private static final long serialVersionUID = 195088469L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QSchoolInformation schoolInformation = new QSchoolInformation("schoolInformation");

    public final com.inhabas.api.domain.member.type.wrapper.QSemester gen;

    public final com.inhabas.api.domain.member.type.wrapper.QGrade grade;

    public final EnumPath<com.inhabas.api.domain.member.type.wrapper.Major> major = createEnum("major", com.inhabas.api.domain.member.type.wrapper.Major.class);

    public QSchoolInformation(String variable) {
        this(SchoolInformation.class, forVariable(variable), INITS);
    }

    public QSchoolInformation(Path<? extends SchoolInformation> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QSchoolInformation(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QSchoolInformation(PathMetadata metadata, PathInits inits) {
        this(SchoolInformation.class, metadata, inits);
    }

    public QSchoolInformation(Class<? extends SchoolInformation> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.gen = inits.isInitialized("gen") ? new com.inhabas.api.domain.member.type.wrapper.QSemester(forProperty("gen")) : null;
        this.grade = inits.isInitialized("grade") ? new com.inhabas.api.domain.member.type.wrapper.QGrade(forProperty("grade")) : null;
    }

}

