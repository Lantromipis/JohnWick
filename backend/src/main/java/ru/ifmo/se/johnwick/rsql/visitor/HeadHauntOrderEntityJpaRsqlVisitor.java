package ru.ifmo.se.johnwick.rsql.visitor;

import ru.ifmo.se.johnwick.model.entity.HeadHuntOrderEntity;
import ru.ifmo.se.johnwick.rsql.field.JpaRsqlFieldProvider;
import ru.ifmo.se.johnwick.rsql.field.enm.JpaRsqlOrderStatusFieldProvider;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class HeadHauntOrderEntityJpaRsqlVisitor extends AbstractJpaRsqlVisitor<HeadHuntOrderEntity> {
    private final static List<JpaRsqlFieldProvider> HEAD_HAUNT_ORDER_FIELDS_PROVIDERS = List.of(
            new JpaRsqlOrderStatusFieldProvider("status")
    );

    private final static Map<String, JpaRsqlFieldProvider> HEAD_HAUNT_ORDER_FIELDS_PROVIDERS_MAP = HEAD_HAUNT_ORDER_FIELDS_PROVIDERS.stream().collect(
            Collectors.toMap(
                    JpaRsqlFieldProvider::getApplicableFieldName,
                    Function.identity()
            )
    );

    public HeadHauntOrderEntityJpaRsqlVisitor() {
        super(HEAD_HAUNT_ORDER_FIELDS_PROVIDERS_MAP);
    }
}
