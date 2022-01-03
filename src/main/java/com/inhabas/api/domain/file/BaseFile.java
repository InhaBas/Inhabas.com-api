package com.inhabas.api.domain.file;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseFile {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Integer id;

    @Column(name = "filepath_legacy")
    protected String legacyPath;

    protected String filename;

    protected String filepath;

    @CreatedDate
    protected LocalDateTime uploaded;
}
