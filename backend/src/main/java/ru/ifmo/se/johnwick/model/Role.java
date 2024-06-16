package ru.ifmo.se.johnwick.model;

import lombok.Getter;

import java.util.Arrays;

public enum Role {
    ADMIN("ADMIN"),
    KILLER("KILLER"),
    TAILOR("TAILOR"),
    SOMMELIER("SOMMELIER"),
    CLEANER("CLEANER");

    @Getter
    private final String string;

    Role(String string) {
        this.string = string;
    }

    public static Role byString(String string) {
        return Arrays.stream(values())
                .filter(role -> role.getString().equals(string))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
