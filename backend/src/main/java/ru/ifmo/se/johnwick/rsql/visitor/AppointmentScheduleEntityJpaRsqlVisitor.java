package ru.ifmo.se.johnwick.rsql.visitor;

import ru.ifmo.se.johnwick.model.entity.AppointmentScheduleEntity;
import ru.ifmo.se.johnwick.model.entity.RegularOrderEntity;
import ru.ifmo.se.johnwick.rsql.field.JpaRsqlFieldProvider;
import ru.ifmo.se.johnwick.rsql.field.JpaRsqlLocalDateJpaRsqlFieldProvider;
import ru.ifmo.se.johnwick.rsql.field.JpaRsqlOffsetTimeJpaRsqlFieldProvider;
import ru.ifmo.se.johnwick.rsql.field.JpaRsqlUUIDFieldProvider;
import ru.ifmo.se.johnwick.rsql.field.enm.JpaRsqlOrderStatusFieldProvider;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class AppointmentScheduleEntityJpaRsqlVisitor extends AbstractJpaRsqlVisitor<AppointmentScheduleEntity> {
    private final static List<JpaRsqlFieldProvider> APPOINTMENT_SCHEDULE_FIELDS_PROVIDERS = List.of(
            new JpaRsqlUUIDFieldProvider("host.id"),
            new JpaRsqlOffsetTimeJpaRsqlFieldProvider("startTime"),
            new JpaRsqlOffsetTimeJpaRsqlFieldProvider("endTime"),
            new JpaRsqlLocalDateJpaRsqlFieldProvider("date")
    );

    private final static Map<String, JpaRsqlFieldProvider> APPOINTMENT_SCHEDULE_FIELDS_PROVIDERS_MAP = APPOINTMENT_SCHEDULE_FIELDS_PROVIDERS
            .stream()
            .collect(
                    Collectors.toMap(
                            JpaRsqlFieldProvider::getApplicableFieldName,
                            Function.identity()
                    )
            );

    public AppointmentScheduleEntityJpaRsqlVisitor() {
        super(APPOINTMENT_SCHEDULE_FIELDS_PROVIDERS_MAP);
    }
}
