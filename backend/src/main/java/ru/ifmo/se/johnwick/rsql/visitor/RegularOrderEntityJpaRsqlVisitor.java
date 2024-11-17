package ru.ifmo.se.johnwick.rsql.visitor;

import ru.ifmo.se.johnwick.model.entity.RegularOrderEntity;
import ru.ifmo.se.johnwick.rsql.field.JpaRsqlFieldProvider;
import ru.ifmo.se.johnwick.rsql.field.JpaRsqlUUIDFieldProvider;
import ru.ifmo.se.johnwick.rsql.field.enm.JpaRsqlOrderStatusFieldProvider;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class RegularOrderEntityJpaRsqlVisitor extends AbstractJpaRsqlVisitor<RegularOrderEntity> {
    private final static List<JpaRsqlFieldProvider> REGULAR_ORDER_FIELDS_PROVIDERS = List.of(
            new JpaRsqlOrderStatusFieldProvider("status"),
            new JpaRsqlUUIDFieldProvider("assignee.id")
    );

    private final static Map<String, JpaRsqlFieldProvider> REGULAR_ORDER_FIELDS_PROVIDERS_MAP = REGULAR_ORDER_FIELDS_PROVIDERS.stream().collect(
            Collectors.toMap(
                    JpaRsqlFieldProvider::getApplicableFieldName,
                    Function.identity()
            )
    );

    public RegularOrderEntityJpaRsqlVisitor() {
        super(REGULAR_ORDER_FIELDS_PROVIDERS_MAP);
    }
}
