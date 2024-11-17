package ru.ifmo.se.johnwick.service.api;

import ru.ifmo.se.johnwick.model.UserRole;
import ru.ifmo.se.johnwick.model.dto.NotificationDto;
import ru.ifmo.se.johnwick.model.entity.UserEntity;

import java.util.List;

public interface NotificationService {
    void sendNotificationToAllUsersByRole(UserRole role, String title, String message);

    void sendNotificationToUser(UserEntity user, String title, String message);

    List<NotificationDto> listCurrentUserNotifications();
}
