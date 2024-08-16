package ru.ifmo.se.johnwick.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import ru.ifmo.se.johnwick.entity.NotificationEntity;
import ru.ifmo.se.johnwick.entity.UserEntity;

import java.util.Collection;

@ApplicationScoped
public class NotificationRepository implements PanacheRepository<NotificationEntity> {
    public Collection<NotificationEntity> findByTargetSortedByCreatedDesc(UserEntity target) {
        return find("target = ?1 order by createdTimestamp desc", target).list();
    }
}
