package ru.ifmo.se.johnwick.rsql.field.enm;

import ru.ifmo.se.johnwick.model.OrderStatus;
import ru.ifmo.se.johnwick.rsql.field.AbstractJpaRsqlFledProvider;

public class JpaRsqlOrderStatusFieldProvider extends AbstractJpaRsqlFledProvider {
    public JpaRsqlOrderStatusFieldProvider(String fieldName) {
        super(fieldName);
    }

    @Override
    public Comparable getTypedValue(String stringValue) {
        return OrderStatus.valueOf(stringValue);
    }
}
