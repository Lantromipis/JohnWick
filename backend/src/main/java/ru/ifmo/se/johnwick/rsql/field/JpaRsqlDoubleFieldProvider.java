package ru.ifmo.se.johnwick.rsql.field;

public class JpaRsqlDoubleFieldProvider extends AbstractJpaRsqlFieldProvider {
    public JpaRsqlDoubleFieldProvider(String fieldName) {
        super(fieldName);
    }

    @Override
    public Comparable getTypedValue(String stringValue) {
        return Double.valueOf(stringValue);
    }
}
