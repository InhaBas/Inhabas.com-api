package com.inhabas.api.domain.member.type.wrapper;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QSemester is a Querydsl query type for Semester
 */
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QSemester extends BeanPath<Semester> {

    private static final long serialVersionUID = -430403228L;

    public static final QSemester semester = new QSemester("semester");

    public final NumberPath<Integer> value = createNumber("value", Integer.class);

    public QSemester(String variable) {
        super(Semester.class, forVariable(variable));
    }

    public QSemester(Path<? extends Semester> path) {
        super(path.getType(), path.getMetadata());
    }

    public QSemester(PathMetadata metadata) {
        super(Semester.class, metadata);
    }

}
