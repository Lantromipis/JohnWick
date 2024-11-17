package ru.ifmo.se.johnwick.rsql.visitor;

import ru.ifmo.se.johnwick.model.entity.RegularOrderApplicationEntity;
import ru.ifmo.se.johnwick.rsql.field.JpaRsqlFieldProvider;
import ru.ifmo.se.johnwick.rsql.field.JpaRsqlUUIDFieldProvider;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class RegularOrderApplicationEntityJpaRsqlVisitor extends AbstractJpaRsqlVisitor<RegularOrderApplicationEntity> {
    private final static List<JpaRsqlFieldProvider> REGULAR_ORDER_APPLICATION_FIELDS_PROVIDERS = List.of(
            new JpaRsqlUUIDFieldProvider("killer.id")
    );

    private final static Map<String, JpaRsqlFieldProvider> REGULAR_ORDER_APPLICATION_FIELDS_PROVIDERS_MAP = REGULAR_ORDER_APPLICATION_FIELDS_PROVIDERS.stream().collect(
            Collectors.toMap(
                    JpaRsqlFieldProvider::getApplicableFieldName,
                    Function.identity()
            )
    );

    public RegularOrderApplicationEntityJpaRsqlVisitor() {
        super(REGULAR_ORDER_APPLICATION_FIELDS_PROVIDERS_MAP);
    }
}
