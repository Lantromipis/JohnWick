package ru.ifmo.se.johnwick.exception;

import lombok.Getter;

@Getter
public class UnknownEntityFieldException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    private String field;

    public UnknownEntityFieldException(String fieldName) {
        super("Unknown entity field with name '" + fieldName + "'");
        this.field = fieldName;
    }
}
