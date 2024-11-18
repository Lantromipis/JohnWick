package ru.ifmo.se.johnwick.rsql.field;

public class JpaRsqlIntegerFieldProvider extends AbstractJpaRsqlFieldProvider {
    public JpaRsqlIntegerFieldProvider(String fieldName) {
        super(fieldName);
    }

    @Override
    public Comparable getTypedValue(String stringValue) {
        return Integer.valueOf(stringValue);
    }
}
