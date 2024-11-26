package ru.ifmo.se.johnwick.rsql.visitor;

import ru.ifmo.se.johnwick.model.entity.CleaningRequestEntity;
import ru.ifmo.se.johnwick.rsql.field.JpaRsqlFieldProvider;
import ru.ifmo.se.johnwick.rsql.field.enm.JpaRsqlCleaningRequestStatusFieldProvider;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CleaningRequestEntityJpaRsqlVisitor extends AbstractJpaRsqlVisitor<CleaningRequestEntity> {
    private final static List<JpaRsqlFieldProvider> CLEANING_REQUEST_FIELDS_PROVIDERS = List.of(
            new JpaRsqlCleaningRequestStatusFieldProvider("status")
    );

    private final static Map<String, JpaRsqlFieldProvider> CLEANING_REQUEST_FIELDS_PROVIDERS_MAP = CLEANING_REQUEST_FIELDS_PROVIDERS.stream().collect(
            Collectors.toMap(
                    JpaRsqlFieldProvider::getApplicableFieldName,
                    Function.identity()
            )
    );

    public CleaningRequestEntityJpaRsqlVisitor() {
        super(CLEANING_REQUEST_FIELDS_PROVIDERS_MAP);
    }
}
