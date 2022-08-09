package com.inhabas.api.domain.file;

import com.inhabas.api.domain.file.type.wrapper.FileName;
import com.inhabas.api.domain.file.type.wrapper.FilePath;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@MappedSuperclass
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseFile {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Integer id;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "filepath_legacy"))
    protected FilePath legacyPath;

    @Embedded
    protected FileName filename;

    @Embedded
    protected FilePath filepath;

    @CreatedDate
    protected LocalDateTime uploaded;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(BaseFile.class.isAssignableFrom(o.getClass()))) return false;
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
