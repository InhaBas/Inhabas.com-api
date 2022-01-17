package com.inhabas.api.domain.member.type.wrapper;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QGrade is a Querydsl query type for Grade
 */
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QGrade extends BeanPath<Grade> {

    private static final long serialVersionUID = 1371148075L;

    public static final QGrade grade = new QGrade("grade");

    public final NumberPath<Integer> value = createNumber("value", Integer.class);

    public QGrade(String variable) {
        super(Grade.class, forVariable(variable));
    }

    public QGrade(Path<? extends Grade> path) {
        super(path.getType(), path.getMetadata());
    }

    public QGrade(PathMetadata metadata) {
        super(Grade.class, metadata);
    }

}

