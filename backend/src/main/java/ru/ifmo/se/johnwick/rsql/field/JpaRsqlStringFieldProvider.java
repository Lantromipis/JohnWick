package ru.ifmo.se.johnwick.rsql.field;

public class JpaRsqlStringFieldProvider extends AbstractJpaRsqlFieldProvider {
    public JpaRsqlStringFieldProvider(String fieldName) {
        super(fieldName);
    }

    @Override
    public Comparable getTypedValue(String stringValue) {
        return stringValue;
    }
}
