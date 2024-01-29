package com.inhabas.api.domain.file.domain;

import java.time.LocalDateTime;
import java.util.Objects;

import javax.persistence.*;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.inhabas.api.domain.file.domain.valueObject.FileName;
import com.inhabas.api.domain.file.domain.valueObject.FileUrl;

@Getter
@MappedSuperclass
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class BaseFile {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  protected Long id;

  @Embedded protected FileName name;

  @Embedded protected FileUrl url;

  @CreatedDate
  @Column(
      nullable = false,
      updatable = false,
      insertable = false,
      columnDefinition = "DATETIME(0) DEFAULT CURRENT_TIMESTAMP")
  protected LocalDateTime dateCreated;

  public BaseFile(String name, String url) {
    this.name = new FileName(name);
    this.url = new FileUrl(url);
    this.dateCreated = LocalDateTime.now();
  }

  public String getName() {
    return this.name.getValue();
  }

  public String getUrl() {
    return this.url.getValue();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(BaseFile.class.isAssignableFrom(o.getClass()))) return false;
    BaseFile baseFile = (BaseFile) o;
    return getId().equals(baseFile.getId())
        && getName().equals(baseFile.getName())
        && Objects.equals(getUrl(), baseFile.getUrl())
        && getDateCreated().equals(baseFile.getDateCreated());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getId(), getName(), getUrl(), getDateCreated());
  }
}
