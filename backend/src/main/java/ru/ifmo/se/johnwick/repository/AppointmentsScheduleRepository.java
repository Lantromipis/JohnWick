package ru.ifmo.se.johnwick.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import ru.ifmo.se.johnwick.model.entity.AppointmentScheduleEntity;
import ru.ifmo.se.johnwick.model.entity.UserEntity;

import java.time.OffsetDateTime;
import java.util.UUID;

@ApplicationScoped
public class AppointmentsScheduleRepository implements PanacheRepositoryBase<AppointmentScheduleEntity, UUID> {
    public boolean existsForRange(UserEntity user, OffsetDateTime start, OffsetDateTime end) {
        return find(
                "SELECT entity FROM AppointmentScheduleEntity entity WHERE host = ?1 AND ((?3 >= startTime AND ?3 <= endTime) OR (?2 >= startTime AND ?2 <= endTime))",
                user,
                start,
                end
        ).count() > 0;
    }
}
