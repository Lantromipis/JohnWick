package ru.ifmo.se.johnwick.rsql.field.enm;

import ru.ifmo.se.johnwick.model.CleaningRequestStatus;
import ru.ifmo.se.johnwick.rsql.field.AbstractJpaRsqlFieldProvider;

public class JpaRsqlCleaningRequestStatusFieldProvider extends AbstractJpaRsqlFieldProvider {
    public JpaRsqlCleaningRequestStatusFieldProvider(String fieldName) {
        super(fieldName);
    }

    @Override
    public Comparable getTypedValue(String stringValue) {
        return CleaningRequestStatus.valueOf(stringValue);
    }
}
