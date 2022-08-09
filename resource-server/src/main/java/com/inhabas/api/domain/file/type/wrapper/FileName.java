package com.inhabas.api.domain.file.type.wrapper;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Transient;
import java.util.Objects;

@Embeddable
public class FileName {

    @Column(name = "filename")
    private String value;

    @Transient
    private final int MAX_LENGTH = 300;

    public FileName() {}

    public FileName(String value) {
        if (validate(value))
            this.value = value;
        else
            throw new IllegalArgumentException();
    }

    private boolean validate(Object value) {
        if (Objects.isNull(value)) return false;
        if (!(value instanceof String)) return false;

        String o = (String) value;
        if (o.isBlank()) return false;
        return o.length() < MAX_LENGTH;
    }

    public String getValue() {
        return this.value;
    }

    public String getExtension() {
        return this.value.substring(this.value.lastIndexOf(".") + 1);
    }

    public String getNameWithoutExtension() {
        return this.value.substring(0, this.value.lastIndexOf("."));
    }
}
