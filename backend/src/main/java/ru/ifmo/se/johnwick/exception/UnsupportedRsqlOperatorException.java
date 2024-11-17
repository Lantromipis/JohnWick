package ru.ifmo.se.johnwick.exception;

import lombok.Getter;

@Getter
public class UnsupportedRsqlOperatorException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    private String symbol;

    public UnsupportedRsqlOperatorException(String symbol) {
        super("RSQL operator with symbol '" + symbol + " is not supported.");
        this.symbol = symbol;
    }
}
