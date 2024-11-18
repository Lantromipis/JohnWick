package ru.ifmo.se.johnwick.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import ru.ifmo.se.johnwick.model.entity.AppointmentScheduleEntity;

import java.util.UUID;

@ApplicationScoped
public class AppointmentsScheduleRepository implements PanacheRepositoryBase<AppointmentScheduleEntity, UUID> {
}
