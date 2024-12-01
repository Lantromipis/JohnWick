package ru.ifmo.se.johnwick.service.impl;

import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import ru.ifmo.se.johnwick.constant.ApiConstant;
import ru.ifmo.se.johnwick.exception.ValidationException;
import ru.ifmo.se.johnwick.model.CleaningRequestStatus;
import ru.ifmo.se.johnwick.model.OrderStatus;
import ru.ifmo.se.johnwick.model.UserRole;
import ru.ifmo.se.johnwick.model.dto.CleaningRequestDto;
import ru.ifmo.se.johnwick.model.entity.CleaningRequestEntity;
import ru.ifmo.se.johnwick.model.entity.OrderEntity;
import ru.ifmo.se.johnwick.model.entity.UserEntity;
import ru.ifmo.se.johnwick.repository.CleaningRequestRepository;
import ru.ifmo.se.johnwick.repository.UserRepository;
import ru.ifmo.se.johnwick.service.api.CleaningRequestService;
import ru.ifmo.se.johnwick.service.api.NotificationService;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@QuarkusTest
class CleaningRequestServiceImplTest {

    @InjectMock
    CleaningRequestRepository cleaningRequestRepository;

    @InjectMock
    UserRepository userRepository;

    @InjectMock
    NotificationService notificationService;

    @Inject
    CleaningRequestService cleaningRequestService;

    public static final String TEST_KILLER_NAME = "testKiller";
    public static final String TEST_CLEANER_NAME = "testCleaner";

    private UserEntity testKillerEntity;
    private UserEntity testCleanerEntity;

    @BeforeEach
    void setUp() {
        testKillerEntity = new UserEntity();
        testKillerEntity.setUsername(TEST_KILLER_NAME);
        testKillerEntity.setId(UUID.randomUUID());
        testKillerEntity.setRole(UserRole.KILLER);

        Mockito.when(userRepository.findByUsername(TEST_KILLER_NAME)).thenReturn(testKillerEntity);

        testCleanerEntity = new UserEntity();
        testCleanerEntity.setUsername(TEST_CLEANER_NAME);
        testCleanerEntity.setId(UUID.randomUUID());
        testCleanerEntity.setRole(UserRole.CLEANER);

        Mockito.when(userRepository.findByUsername(TEST_CLEANER_NAME)).thenReturn(testCleanerEntity);
    }

    @Test
    void testCreateCleaningRequest() {
        OrderEntity order = new OrderEntity();
        order.setId(UUID.randomUUID());

        cleaningRequestService.createCleaningForOrder(order, testKillerEntity);

        ArgumentCaptor<CleaningRequestEntity> cleaningCaptor = ArgumentCaptor.forClass(CleaningRequestEntity.class);
        Mockito.verify(cleaningRequestRepository).persistAndFlush(cleaningCaptor.capture());

        // test created cleaning request
        CleaningRequestEntity cleaningRequestEntity = cleaningCaptor.getValue();
        assertEquals(cleaningRequestEntity.getStatus(), CleaningRequestStatus.CREATED);
        assertEquals(cleaningRequestEntity.getRequestedBy(), testKillerEntity);
        assertEquals(cleaningRequestEntity.getOrder(), order);

        // test notification service called
        Mockito.verify(notificationService, Mockito.times(1)).sendNotificationToAllUsersByRole(
                Mockito.eq(UserRole.CLEANER),
                Mockito.anyString(),
                Mockito.anyString()
        );
    }

    @Test
    @TestSecurity(user = TEST_CLEANER_NAME, roles = {ApiConstant.ROLE_CLEANER})
    void testUpdateCleaningRequestStatusInProgress() {
        UUID requestId = UUID.randomUUID();

        CleaningRequestEntity cleaningRequestEntity = new CleaningRequestEntity();
        cleaningRequestEntity.setId(requestId);

        Mockito.when(cleaningRequestRepository.findById(requestId)).thenReturn(cleaningRequestEntity);

        CleaningRequestDto cleaningToUpdate = new CleaningRequestDto();
        cleaningToUpdate.setId(requestId);
        cleaningToUpdate.setStatus(CleaningRequestStatus.IN_PROGRESS);

        // test ok case
        cleaningRequestEntity.setStatus(CleaningRequestStatus.CREATED);
        CleaningRequestDto updatedCleaningRequest = cleaningRequestService.updateCleaning(cleaningToUpdate);
        assertEquals(updatedCleaningRequest.getStatus(), CleaningRequestStatus.IN_PROGRESS);

        // test wrong status
        cleaningRequestEntity.setStatus(CleaningRequestStatus.CREATED);
        assertThrows(ValidationException.class, () -> cleaningRequestService.updateCleaning(cleaningToUpdate));

        // test already taken
        cleaningRequestEntity.setAppliedCleaner(new UserEntity());
        assertThrows(ValidationException.class, () -> cleaningRequestService.updateCleaning(cleaningToUpdate));
    }

    @Test
    @TestSecurity(user = TEST_CLEANER_NAME, roles = {ApiConstant.ROLE_CLEANER})
    void testUpdateCleaningRequestStatusCompleted() {
        UUID requestId = UUID.randomUUID();

        CleaningRequestEntity cleaningRequestEntity = new CleaningRequestEntity();
        cleaningRequestEntity.setId(requestId);
        cleaningRequestEntity.setRequestedBy(testKillerEntity);
        cleaningRequestEntity.setAppliedCleaner(testCleanerEntity);

        OrderEntity order = new OrderEntity();
        order.setId(UUID.randomUUID());

        cleaningRequestEntity.setOrder(order);

        Mockito.when(cleaningRequestRepository.findById(requestId)).thenReturn(cleaningRequestEntity);

        CleaningRequestDto cleaningToUpdate = new CleaningRequestDto();
        cleaningToUpdate.setId(requestId);
        cleaningToUpdate.setStatus(CleaningRequestStatus.COMPLETED);

        // test ok case
        cleaningRequestEntity.setStatus(CleaningRequestStatus.IN_PROGRESS);
        CleaningRequestDto updatedCleaningRequest = cleaningRequestService.updateCleaning(cleaningToUpdate);
        assertEquals(updatedCleaningRequest.getStatus(), CleaningRequestStatus.COMPLETED);
        assertEquals(updatedCleaningRequest.getOrder().getStatus(), OrderStatus.AWAITING_APPROVAL);

        // test notification service called
        Mockito.verify(notificationService, Mockito.times(1)).sendNotificationToUser(
                Mockito.eq(testKillerEntity),
                Mockito.anyString(),
                Mockito.anyString()
        );

        // test wrong status
        cleaningRequestEntity.setStatus(CleaningRequestStatus.CREATED);
        assertThrows(ValidationException.class, () -> cleaningRequestService.updateCleaning(cleaningToUpdate));

        // test already taken
        cleaningRequestEntity.setAppliedCleaner(new UserEntity());
        assertThrows(ValidationException.class, () -> cleaningRequestService.updateCleaning(cleaningToUpdate));
    }
}