package ru.ifmo.se.johnwick.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import ru.ifmo.se.johnwick.model.entity.AppointmentEntity;
import ru.ifmo.se.johnwick.model.entity.UserEntity;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class AppointmentRepository implements PanacheRepositoryBase<AppointmentEntity, UUID> {
    public List<AppointmentEntity> findByUser(UserEntity user) {
        return find("SELECT entity FROM AppointmentEntity entity WHERE bookedBy = ?1 ORDER BY startTime", user).list();
    }

    public long deleteAppointment(UUID id) {
        return delete("id", id);
    }
}
