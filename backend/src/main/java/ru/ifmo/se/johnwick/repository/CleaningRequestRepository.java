package ru.ifmo.se.johnwick.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import ru.ifmo.se.johnwick.entity.CleaningRequestEntity;
import ru.ifmo.se.johnwick.entity.UserEntity;

import java.util.UUID;

@ApplicationScoped
public class CleaningRequestRepository implements PanacheRepository<CleaningRequestEntity> {

    public CleaningRequestEntity findByUUID(UUID id) {
        return find("id", id).firstResult();
    }
}
