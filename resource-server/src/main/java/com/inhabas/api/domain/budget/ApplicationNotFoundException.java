package com.inhabas.api.domain.budget;

public class ApplicationNotFoundException extends RuntimeException {

    public ApplicationNotFoundException() {
        super("cannot find such budget support application!");
    }

    public ApplicationNotFoundException(String message) {
        super(message);
    }
}
