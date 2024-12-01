package ru.ifmo.se.johnwick.service.impl;

import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.ifmo.se.johnwick.mapper.NotificationMapper;
import ru.ifmo.se.johnwick.model.UserRole;
import ru.ifmo.se.johnwick.model.entity.NotificationEntity;
import ru.ifmo.se.johnwick.model.entity.UserEntity;
import ru.ifmo.se.johnwick.repository.NotificationRepository;
import ru.ifmo.se.johnwick.repository.UserRepository;
import ru.ifmo.se.johnwick.service.api.NotificationService;

import java.util.List;

@QuarkusTest
class NotificationServiceImplTest {

    @InjectMock
    NotificationRepository notificationRepository;

    @InjectMock
    UserRepository userRepository;

    @InjectMock
    NotificationMapper notificationMapper;

    @Inject
    NotificationService notificationService;

    @Test
    void testSendToAllUsers() {
        UserEntity user1 = new UserEntity();
        UserEntity user2 = new UserEntity();
        UserEntity user3 = new UserEntity();

        Mockito.when(userRepository.findAllByRole(UserRole.KILLER)).thenReturn(List.of(user1, user2, user3));

        notificationService.sendNotificationToAllUsersByRole(UserRole.KILLER, "test", "test");
        Mockito.verify(notificationRepository, Mockito.times(3)).persist((NotificationEntity) Mockito.any());
    }

    @Test
    void testSendToUser() {
        UserEntity user = new UserEntity();
        notificationService.sendNotificationToUser(user, "test", "test");
        Mockito.verify(notificationRepository, Mockito.times(1)).persist((NotificationEntity) Mockito.any());
    }
}