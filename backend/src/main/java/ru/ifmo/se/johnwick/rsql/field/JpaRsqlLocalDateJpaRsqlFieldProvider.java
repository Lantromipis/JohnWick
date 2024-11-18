package ru.ifmo.se.johnwick.rsql.field;

import java.time.LocalDate;

public class JpaRsqlLocalDateJpaRsqlFieldProvider extends AbstractJpaRsqlFieldProvider {
    public JpaRsqlLocalDateJpaRsqlFieldProvider(String fieldName) {
        super(fieldName);
    }

    @Override
    public Comparable getTypedValue(String stringValue) {
        return LocalDate.parse(stringValue);
    }
}
