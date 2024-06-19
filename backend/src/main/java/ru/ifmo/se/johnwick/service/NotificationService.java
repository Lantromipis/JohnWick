package ru.ifmo.se.johnwick.service;

import io.quarkus.qute.Location;
import io.quarkus.qute.Template;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import ru.ifmo.se.johnwick.entity.NotificationEntity;
import ru.ifmo.se.johnwick.entity.UserEntity;
import ru.ifmo.se.johnwick.model.Role;
import ru.ifmo.se.johnwick.model.dto.OrderApplicationDto;
import ru.ifmo.se.johnwick.model.dto.OrderDto;

import java.util.Collection;
import java.util.Collections;

@ApplicationScoped
public class NotificationService {
    @Location("headHuntOrderCreatedNotification.txt")
    Template headHuntOrderCreated;

    @Location("promissoryNoteCreatedNotification.txt")
    Template promissoryNoteCreated;

    @Location("regularOrderCreatedNotification.txt")
    Template regularOrderCreated;

    @Location("orderApplicationAcceptedNotification.txt")
    Template orderApplicationAccepted;

    public void notifyAboutOrderCreation(OrderDto orderDto) {
        Template template = null;
        Collection<UserEntity> targets = null;
        switch (orderDto.getType()) {
            case REGULAR -> {
                template = regularOrderCreated;
                targets = UserEntity.findByRole(Role.KILLER);
            }
            case HEAD_HUNT -> {
                template = headHuntOrderCreated;
                targets = UserEntity.findByRole(Role.KILLER);
            }
            case PROMISSORY_NOTE -> {
                template = promissoryNoteCreated;
                UserEntity assignee = UserEntity.findByUsername(orderDto.getAssignee().getUsername());
                targets = Collections.singletonList(assignee);
            }
        };

        String title = template.data("order", orderDto).render();
        createNotifications(title, targets);
    }

    public void notifyAboutAcceptedApplication(OrderApplicationDto applicationDto) {
        UserEntity assignee = UserEntity.findByUsername(applicationDto.getAppliedKiller().getUsername());
        String title = orderApplicationAccepted.data("order", applicationDto.getOrder()).render();
        createNotifications(title, Collections.singletonList(assignee));
    }

    @Transactional
    void createNotifications(String title, Collection<UserEntity> targets) {
        for (UserEntity target : targets) {
            NotificationEntity notificationEntity = new NotificationEntity(title, target);
            notificationEntity.persist();
        }
    }
}
