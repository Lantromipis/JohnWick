package ru.ifmo.se.johnwick.rsql.field;

public class JpaRsqLongFieldProvider extends AbstractJpaRsqlFledProvider {
    public JpaRsqLongFieldProvider(String fieldName) {
        super(fieldName);
    }

    @Override
    public Comparable getTypedValue(String stringValue) {
        return Integer.valueOf(stringValue);
    }
}
