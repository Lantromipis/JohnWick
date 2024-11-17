package ru.ifmo.se.johnwick.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import ru.ifmo.se.johnwick.model.entity.RegularOrderApplicationEntity;
import ru.ifmo.se.johnwick.model.entity.RegularOrderEntity;
import ru.ifmo.se.johnwick.model.entity.UserEntity;

import java.util.UUID;

@ApplicationScoped
public class RegularOrderApplicationRepository implements PanacheRepositoryBase<RegularOrderApplicationEntity, UUID> {
    public RegularOrderApplicationEntity findByOrderAndUser(RegularOrderEntity order, UserEntity user) {
        return find("regularOrder = ?1 and killer = ?2", order, user).firstResult();
    }
}
