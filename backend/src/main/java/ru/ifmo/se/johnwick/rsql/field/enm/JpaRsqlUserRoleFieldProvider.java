package ru.ifmo.se.johnwick.rsql.field.enm;

import ru.ifmo.se.johnwick.model.UserRole;
import ru.ifmo.se.johnwick.rsql.field.AbstractJpaRsqlFieldProvider;

public class JpaRsqlUserRoleFieldProvider extends AbstractJpaRsqlFieldProvider {
    public JpaRsqlUserRoleFieldProvider(String fieldName) {
        super(fieldName);
    }

    @Override
    public Comparable getTypedValue(String stringValue) {
        return UserRole.valueOf(stringValue);
    }
}
