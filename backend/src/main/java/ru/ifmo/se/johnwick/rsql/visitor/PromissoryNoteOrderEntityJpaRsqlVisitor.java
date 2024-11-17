package ru.ifmo.se.johnwick.rsql.visitor;

import ru.ifmo.se.johnwick.model.entity.PromissoryNoteOrderEntity;
import ru.ifmo.se.johnwick.rsql.field.JpaRsqlFieldProvider;
import ru.ifmo.se.johnwick.rsql.field.JpaRsqlUUIDFieldProvider;
import ru.ifmo.se.johnwick.rsql.field.enm.JpaRsqlOrderStatusFieldProvider;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class PromissoryNoteOrderEntityJpaRsqlVisitor extends AbstractJpaRsqlVisitor<PromissoryNoteOrderEntity> {
    private final static List<JpaRsqlFieldProvider> PROMISSORY_NOTE_ORDER_FIELDS_PROVIDERS = List.of(
            new JpaRsqlOrderStatusFieldProvider("status"),
            new JpaRsqlUUIDFieldProvider("debtor.id")
    );

    private final static Map<String, JpaRsqlFieldProvider> PROMISSORY_NOTE_ORDER_FIELDS_PROVIDERS_MAP = PROMISSORY_NOTE_ORDER_FIELDS_PROVIDERS.stream().collect(
            Collectors.toMap(
                    JpaRsqlFieldProvider::getApplicableFieldName,
                    Function.identity()
            )
    );

    public PromissoryNoteOrderEntityJpaRsqlVisitor() {
        super(PROMISSORY_NOTE_ORDER_FIELDS_PROVIDERS_MAP);
    }
}
