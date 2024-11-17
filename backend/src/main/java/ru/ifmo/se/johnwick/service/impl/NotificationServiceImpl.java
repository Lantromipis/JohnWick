package ru.ifmo.se.johnwick.service.impl;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.SecurityContext;
import ru.ifmo.se.johnwick.mapper.NotificationMapper;
import ru.ifmo.se.johnwick.model.UserRole;
import ru.ifmo.se.johnwick.model.dto.NotificationDto;
import ru.ifmo.se.johnwick.model.entity.NotificationEntity;
import ru.ifmo.se.johnwick.model.entity.UserEntity;
import ru.ifmo.se.johnwick.repository.NotificationRepository;
import ru.ifmo.se.johnwick.repository.UserRepository;
import ru.ifmo.se.johnwick.service.api.NotificationService;

import java.time.OffsetDateTime;
import java.util.List;

@ApplicationScoped
public class NotificationServiceImpl implements NotificationService {

    @Inject
    NotificationRepository notificationRepository;

    @Inject
    UserRepository userRepository;

    @Context
    SecurityContext securityContext;

    @Inject
    NotificationMapper notificationMapper;

    @Override
    @Transactional()
    public void sendNotificationToAllUsersByRole(UserRole role, String title, String message) {
        List<UserEntity> users = userRepository.findAllByRole(role);
        OffsetDateTime timestamp = OffsetDateTime.now();

        for (UserEntity user : users) {
            NotificationEntity notificationEntity = new NotificationEntity();
            notificationEntity.setTitle(title);
            notificationEntity.setMessage(message);
            notificationEntity.setCreatedTimestamp(timestamp);
            notificationEntity.setRecipient(user);

            notificationRepository.persist(notificationEntity);
        }
    }

    @Override
    @Transactional
    public void sendNotificationToUser(UserEntity user, String title, String message) {
        NotificationEntity notificationEntity = new NotificationEntity();

        notificationEntity.setTitle(title);
        notificationEntity.setMessage(message);
        notificationEntity.setCreatedTimestamp(OffsetDateTime.now());
        notificationEntity.setRecipient(user);

        notificationRepository.persist(notificationEntity);
    }

    @Override
    public List<NotificationDto> listCurrentUserNotifications() {
        UserEntity user = userRepository.findByUsername(securityContext.getUserPrincipal().getName());
        List<NotificationEntity> notifications = notificationRepository.findAllByRecipient(user);
        return notificationMapper.toDto(notifications);
    }
}
