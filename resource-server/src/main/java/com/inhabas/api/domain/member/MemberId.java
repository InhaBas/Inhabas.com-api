package com.inhabas.api.domain.member;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberId implements Serializable {

    private static final long serialVersionUID = -2924578165705238561L;

    @Column(name = "id")
    private Integer id;

    public MemberId(Integer id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MemberId memberId = (MemberId) o;
        return id.equals(memberId.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
