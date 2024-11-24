package ru.ifmo.se.johnwick.rsql.field;

import java.time.OffsetDateTime;

public class JpaRsqlOffsetDateTimeJpaRsqlFieldProvider extends AbstractJpaRsqlFieldProvider {
    public JpaRsqlOffsetDateTimeJpaRsqlFieldProvider(String fieldName) {
        super(fieldName);
    }

    @Override
    public Comparable getTypedValue(String stringValue) {
        return OffsetDateTime.parse(stringValue);
    }
}
