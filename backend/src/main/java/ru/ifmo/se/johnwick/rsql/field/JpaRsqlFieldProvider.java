package ru.ifmo.se.johnwick.rsql.field;

import jakarta.persistence.criteria.Path;
import ru.ifmo.se.johnwick.rsql.JpaRsqlVisitorParams;

public interface JpaRsqlFieldProvider {
    String getApplicableFieldName();

    Path getFieldPath(JpaRsqlVisitorParams rsqlVisitorParams);

    Comparable getTypedValue(String stringValue);
}
