package ru.ifmo.se.johnwick.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import ru.ifmo.se.johnwick.model.entity.NotificationEntity;
import ru.ifmo.se.johnwick.model.entity.UserEntity;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class NotificationRepository implements PanacheRepositoryBase<NotificationEntity, UUID> {
    public List<NotificationEntity> findAllByRecipient(UserEntity recipient) {
        return find("recipient = ?1", recipient).list();
    }
}
