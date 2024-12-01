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
import ru.ifmo.se.johnwick.model.OrderStatus;
import ru.ifmo.se.johnwick.model.OrderType;
import ru.ifmo.se.johnwick.model.UserRole;
import ru.ifmo.se.johnwick.model.dto.HeadHuntOrderDto;
import ru.ifmo.se.johnwick.model.entity.HeadHuntOrderEntity;
import ru.ifmo.se.johnwick.model.entity.UserEntity;
import ru.ifmo.se.johnwick.repository.HeadHuntOrderRepository;
import ru.ifmo.se.johnwick.repository.UserRepository;
import ru.ifmo.se.johnwick.service.api.CleaningRequestService;
import ru.ifmo.se.johnwick.service.api.HeadHuntOrderService;
import ru.ifmo.se.johnwick.service.api.NotificationService;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@QuarkusTest
class HeadHuntOrderServiceImplTest {

    @InjectMock
    HeadHuntOrderRepository headHuntOrderRepository;

    @InjectMock
    NotificationService notificationService;

    @InjectMock
    UserRepository userRepository;

    @InjectMock
    CleaningRequestService cleaningRequestService;

    @Inject
    HeadHuntOrderService headHuntOrderService;

    public static final String CUSTOMER_NAME = "customer";
    public static final String TARGET_NAME = "target";

    public static final String TEST_KILLER_NAME = "testKiller";
    public static final String TEST_ADMIN_NAME = "testAdmin";

    private UserEntity testKillerEntity;
    private UserEntity testAdminEntity;

    @BeforeEach
    void setUp() {
        testKillerEntity = new UserEntity();
        testKillerEntity.setUsername(TEST_KILLER_NAME);
        testKillerEntity.setId(UUID.randomUUID());
        testKillerEntity.setRole(UserRole.KILLER);

        Mockito.when(userRepository.findByUsername(TEST_KILLER_NAME)).thenReturn(testKillerEntity);

        testAdminEntity = new UserEntity();
        testAdminEntity.setUsername(TEST_ADMIN_NAME);
        testAdminEntity.setId(UUID.randomUUID());
        testAdminEntity.setRole(UserRole.ADMIN);

        Mockito.when(userRepository.findByUsername(TEST_ADMIN_NAME)).thenReturn(testAdminEntity);
    }

    @Test
    void testCreateRegularOrder() {
        Mockito.doAnswer(invocation -> {
                    HeadHuntOrderEntity headHuntOrderEntity = (HeadHuntOrderEntity) invocation.getArguments()[0];
                    headHuntOrderEntity.setId(UUID.randomUUID());

                    return null;
                }
        ).when(headHuntOrderRepository).persist((HeadHuntOrderEntity) Mockito.any());

        HeadHuntOrderDto orderToCreate = new HeadHuntOrderDto();
        orderToCreate.setCustomerName(CUSTOMER_NAME);
        orderToCreate.setTargetName(TARGET_NAME);
        orderToCreate.setType(OrderType.HEAD_HUNT);
        orderToCreate.setCurrentPrice(1);

        HeadHuntOrderDto createdOrder = headHuntOrderService.createHeadHuntOrder(orderToCreate);
        assertEquals(orderToCreate.getCustomerName(), createdOrder.getCustomerName());
        assertEquals(orderToCreate.getTargetName(), createdOrder.getTargetName());

        // test notification service called
        Mockito.verify(notificationService, Mockito.times(1)).sendNotificationToAllUsersByRole(
                Mockito.eq(UserRole.KILLER),
                Mockito.anyString(),
                Mockito.anyString()
        );
    }

    @Test
    void testCreateOrderPayloadValidations() {
        HeadHuntOrderDto orderToCreate = new HeadHuntOrderDto();
        orderToCreate.setType(OrderType.HEAD_HUNT);

        assertThrows(ValidationException.class, () -> headHuntOrderService.createHeadHuntOrder(orderToCreate));

        orderToCreate.setCustomerName(CUSTOMER_NAME);
        assertThrows(ValidationException.class, () -> headHuntOrderService.createHeadHuntOrder(orderToCreate));

        orderToCreate.setTargetName(TARGET_NAME);
        assertThrows(ValidationException.class, () -> headHuntOrderService.createHeadHuntOrder(orderToCreate));

        orderToCreate.setCurrentPrice(-1);
        assertThrows(ValidationException.class, () -> headHuntOrderService.createHeadHuntOrder(orderToCreate));

        orderToCreate.setCurrentPrice(0);
        assertThrows(ValidationException.class, () -> headHuntOrderService.createHeadHuntOrder(orderToCreate));
    }

    @Test
    @TestSecurity(user = TEST_KILLER_NAME, roles = {ApiConstant.ROLE_KILLER})
    void testUpdateHeadHuntStatusToAwaitingCleaning() {
        UUID orderId = UUID.randomUUID();

        HeadHuntOrderEntity headHuntOrderEntity = new HeadHuntOrderEntity();
        headHuntOrderEntity.setId(orderId);
        headHuntOrderEntity.setType(OrderType.HEAD_HUNT);

        Mockito.when(headHuntOrderRepository.findById(orderId)).thenReturn(headHuntOrderEntity);

        HeadHuntOrderDto orderToUpdate = new HeadHuntOrderDto();
        orderToUpdate.setId(orderId);

        // ok case
        headHuntOrderEntity.setStatus(OrderStatus.AWAITING_SUBMISSION);
        orderToUpdate.setStatus(OrderStatus.AWAITING_CLEANING);
        HeadHuntOrderDto updatedOrder = headHuntOrderService.updateHeadHuntOrder(orderToUpdate);
        assertEquals(updatedOrder.getStatus(), OrderStatus.AWAITING_CLEANING);
        assertEquals(updatedOrder.getSucceededKiller().getId(), testKillerEntity.getId());

        ArgumentCaptor<HeadHuntOrderEntity> orderCaptor = ArgumentCaptor.forClass(HeadHuntOrderEntity.class);
        ArgumentCaptor<UserEntity> userCaptor = ArgumentCaptor.forClass(UserEntity.class);
        Mockito.verify(cleaningRequestService).createCleaningForOrder(orderCaptor.capture(), userCaptor.capture());

        assertEquals(orderCaptor.getValue(), headHuntOrderEntity);
        assertEquals(userCaptor.getValue(), testKillerEntity);

        // wrong status case
        headHuntOrderEntity.setStatus(OrderStatus.AWAITING_APPLICATIONS);
        orderToUpdate.setStatus(OrderStatus.AWAITING_CLEANING);
        assertThrows(ValidationException.class, () -> headHuntOrderService.updateHeadHuntOrder(orderToUpdate));
    }

    @Test
    @TestSecurity(user = TEST_ADMIN_NAME, roles = {ApiConstant.ROLE_ADMIN})
    void testUpdateHeadHuntStatusToCompleted() {
        UUID orderId = UUID.randomUUID();

        HeadHuntOrderEntity headHuntOrderEntity = new HeadHuntOrderEntity();
        headHuntOrderEntity.setId(orderId);
        headHuntOrderEntity.setType(OrderType.HEAD_HUNT);
        headHuntOrderEntity.setSucceededKiller(testKillerEntity);

        Mockito.when(headHuntOrderRepository.findById(orderId)).thenReturn(headHuntOrderEntity);

        HeadHuntOrderDto orderToUpdate = new HeadHuntOrderDto();
        orderToUpdate.setId(orderId);

        // ok case
        headHuntOrderEntity.setStatus(OrderStatus.AWAITING_APPROVAL);
        orderToUpdate.setStatus(OrderStatus.COMPLETED);
        HeadHuntOrderDto updatedOrder = headHuntOrderService.updateHeadHuntOrder(orderToUpdate);
        assertEquals(updatedOrder.getStatus(), OrderStatus.COMPLETED);

        // test notification service called
        Mockito.verify(notificationService, Mockito.times(1)).sendNotificationToUser(
                Mockito.eq(testKillerEntity),
                Mockito.anyString(),
                Mockito.anyString()
        );

        // wrong status case
        headHuntOrderEntity.setStatus(OrderStatus.AWAITING_APPLICATIONS);
        orderToUpdate.setStatus(OrderStatus.COMPLETED);
        assertThrows(ValidationException.class, () -> headHuntOrderService.updateHeadHuntOrder(orderToUpdate));
    }
}