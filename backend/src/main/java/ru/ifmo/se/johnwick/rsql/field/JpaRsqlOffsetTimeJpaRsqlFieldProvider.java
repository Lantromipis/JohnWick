package ru.ifmo.se.johnwick.rsql.field;

import java.time.OffsetTime;

public class JpaRsqlOffsetTimeJpaRsqlFieldProvider extends AbstractJpaRsqlFieldProvider {
    public JpaRsqlOffsetTimeJpaRsqlFieldProvider(String fieldName) {
        super(fieldName);
    }

    @Override
    public Comparable getTypedValue(String stringValue) {
        return OffsetTime.parse(stringValue);
    }
}
