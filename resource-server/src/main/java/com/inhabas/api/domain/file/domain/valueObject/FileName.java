package com.inhabas.api.domain.file.domain.valueObject;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Transient;

import static com.inhabas.api.global.util.FileUtil.isValidFileName;


@Embeddable
public class FileName {

    @Column(name = "name")
    private String value;

    @Transient
    private final int MAX_LENGTH = 300;


    public FileName(String value) {
        if (validate(value))
            this.value = value;
        else
            throw new IllegalArgumentException();
    }

    public FileName() {}

    public String getValue() {
        return this.value;
    }

    private boolean validate(Object value) {
        return isValidFileName((String) value);
    }

}
