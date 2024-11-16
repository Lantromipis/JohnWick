package ru.ifmo.se.johnwick.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import ru.ifmo.se.johnwick.entity.CleaningRequestEntity;

@ApplicationScoped
public class CleaningRequestRepository implements PanacheRepository<CleaningRequestEntity> {
}
