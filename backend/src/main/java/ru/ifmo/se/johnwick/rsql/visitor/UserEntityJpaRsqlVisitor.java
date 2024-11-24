package ru.ifmo.se.johnwick.rsql.visitor;

import ru.ifmo.se.johnwick.model.entity.UserEntity;
import ru.ifmo.se.johnwick.rsql.field.JpaRsqlFieldProvider;
import ru.ifmo.se.johnwick.rsql.field.enm.JpaRsqlUserRoleFieldProvider;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class UserEntityJpaRsqlVisitor extends AbstractJpaRsqlVisitor<UserEntity> {
    private final static List<JpaRsqlFieldProvider> USER_FIELDS_PROVIDERS = List.of(
            new JpaRsqlUserRoleFieldProvider("role")
    );

    private final static Map<String, JpaRsqlFieldProvider> USER_FIELDS_PROVIDERS_MAP = USER_FIELDS_PROVIDERS.stream().collect(
            Collectors.toMap(
                    JpaRsqlFieldProvider::getApplicableFieldName,
                    Function.identity()
            )
    );

    public UserEntityJpaRsqlVisitor() {
        super(USER_FIELDS_PROVIDERS_MAP);
    }
}
