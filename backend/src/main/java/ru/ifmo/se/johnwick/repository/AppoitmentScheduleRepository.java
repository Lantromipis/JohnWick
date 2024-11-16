package ru.ifmo.se.johnwick.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import ru.ifmo.se.johnwick.entity.AppointmentScheduleEntity;
import ru.ifmo.se.johnwick.entity.UserEntity;
import ru.ifmo.se.johnwick.model.Role;

import java.time.LocalDate;
import java.util.Collection;

@ApplicationScoped
public class AppoitmentScheduleRepository implements PanacheRepository<AppointmentScheduleEntity> {

    public Collection<AppointmentScheduleEntity> findByDate(LocalDate startDate,LocalDate endDate) {
        return find("date >= ?1 and date <= ?2", startDate, endDate).list();
    }
}
