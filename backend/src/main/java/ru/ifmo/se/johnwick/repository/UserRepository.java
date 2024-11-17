package ru.ifmo.se.johnwick.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import ru.ifmo.se.johnwick.model.UserRole;
import ru.ifmo.se.johnwick.model.entity.UserEntity;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class UserRepository implements PanacheRepositoryBase<UserEntity, UUID> {
    public UserEntity findByUsername(String username) {
        return find("username", username).firstResult();
    }

    public List<UserEntity> findAllByRole(UserRole role) {
        return find("role = ?1", role).list();
    }
}
