package ru.ifmo.se.johnwick.rsql.field.enm;

import ru.ifmo.se.johnwick.model.OrderStatus;
import ru.ifmo.se.johnwick.rsql.field.AbstractJpaRsqlFieldProvider;

public class JpaRsqlOrderStatusFieldProvider extends AbstractJpaRsqlFieldProvider {
    public JpaRsqlOrderStatusFieldProvider(String fieldName) {
        super(fieldName);
    }

    @Override
    public Comparable getTypedValue(String stringValue) {
        return OrderStatus.valueOf(stringValue);
    }
}
