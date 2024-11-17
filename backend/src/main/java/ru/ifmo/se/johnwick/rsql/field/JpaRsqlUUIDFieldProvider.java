package ru.ifmo.se.johnwick.rsql.field;

import java.util.UUID;

public class JpaRsqlUUIDFieldProvider extends AbstractJpaRsqlFledProvider {
    public JpaRsqlUUIDFieldProvider(String fieldName) {
        super(fieldName);
    }

    @Override
    public Comparable getTypedValue(String stringValue) {
        return UUID.fromString(stringValue);
    }
}
