package ru.ifmo.se.johnwick.rsql.field;

import jakarta.persistence.criteria.Path;
import ru.ifmo.se.johnwick.rsql.JpaRsqlVisitorParams;

public abstract class AbstractJpaRsqlFledProvider implements JpaRsqlFieldProvider {
    private final String fieldName;

    public AbstractJpaRsqlFledProvider(String fieldName) {
        this.fieldName = fieldName;
    }

    @Override
    public String getApplicableFieldName() {
        return fieldName;
    }

    @Override
    public Path getFieldPath(JpaRsqlVisitorParams rsqlVisitorParams) {
        if (fieldName.contains(".")) {
            String[] parts = fieldName.split("\\.");
            return rsqlVisitorParams.getRoot().get(parts[0]).get(parts[1]);
/*            Join join = rsqlVisitorParams.getRoot().join(parts[0], JoinType.LEFT);
            return join.get(parts[1]);*/
        } else {
            return rsqlVisitorParams.getRoot().get(fieldName);
        }
    }
}
