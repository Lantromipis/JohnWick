package ru.ifmo.se.johnwick.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import ru.ifmo.se.johnwick.entity.UserEntity;
import ru.ifmo.se.johnwick.model.Role;

import java.util.Collection;

@ApplicationScoped
public class UserRepository implements PanacheRepository<UserEntity> {
    public UserEntity findByUsername(String username) {
        return find("username", username).firstResult();
    }

    public Collection<UserEntity> findByPage(Page page) {
        return findAll().page(page).list();
    }

    public Collection<UserEntity> findByRole(Role role) {
        return find("role = ?1", role).list();
    }
}
