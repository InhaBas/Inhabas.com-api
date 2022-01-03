package com.inhabas.api.domain.file;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BaseFile)) return false;
        BaseFile baseFile = (BaseFile) o;
        return getId().equals(baseFile.getId())
                && Objects.equals(getLegacyPath(), baseFile.getLegacyPath())
                && getFilename().equals(baseFile.getFilename())
                && Objects.equals(getFilepath(), baseFile.getFilepath())
                && getUploaded().equals(baseFile.getUploaded());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getLegacyPath(), getFilename(), getFilepath(), getUploaded());
    }
}
