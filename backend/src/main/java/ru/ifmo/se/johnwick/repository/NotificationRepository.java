package ru.ifmo.se.johnwick.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import ru.ifmo.se.johnwick.model.entity.NotificationEntity;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class NotificationRepository implements PanacheRepositoryBase<NotificationEntity, UUID> {
    public List<NotificationEntity> findAllByRecipientUsername(String recipientUsername) {
        return find(
                "SELECT entity FROM NotificationEntity entity WHERE recipient.username = ?1 ORDER BY createdTimestamp DESC",
                recipientUsername
        ).list();
    }
}
