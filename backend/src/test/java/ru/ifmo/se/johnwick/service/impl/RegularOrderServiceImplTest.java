package ru.ifmo.se.johnwick.service.impl;

import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import jakarta.inject.Inject;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import ru.ifmo.se.johnwick.constant.ApiConstant;
import ru.ifmo.se.johnwick.exception.EntityNotFoundByIdException;
import ru.ifmo.se.johnwick.exception.ValidationException;
import ru.ifmo.se.johnwick.model.OrderStatus;
import ru.ifmo.se.johnwick.model.OrderType;
import ru.ifmo.se.johnwick.model.UserRole;
import ru.ifmo.se.johnwick.model.dto.RegularOrderApplicationDto;
import ru.ifmo.se.johnwick.model.dto.RegularOrderDto;
import ru.ifmo.se.johnwick.model.dto.UserDto;
import ru.ifmo.se.johnwick.model.entity.RegularOrderApplicationEntity;
import ru.ifmo.se.johnwick.model.entity.RegularOrderEntity;
import ru.ifmo.se.johnwick.model.entity.UserEntity;
import ru.ifmo.se.johnwick.repository.RegularOrderApplicationRepository;
import ru.ifmo.se.johnwick.repository.RegularOrderRepository;
import ru.ifmo.se.johnwick.repository.UserRepository;
import ru.ifmo.se.johnwick.service.api.CleaningRequestService;
import ru.ifmo.se.johnwick.service.api.NotificationService;
import ru.ifmo.se.johnwick.service.api.RegularOrderService;
import ru.ifmo.se.johnwick.utils.RsqlParserUtils;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class RegularOrderServiceImplTest {

    @InjectMock
    RegularOrderApplicationRepository regularOrderApplicationRepository;

    @InjectMock
    RegularOrderRepository regularOrderRepository;

    @InjectMock
    UserRepository userRepository;

    @InjectMock
    RsqlParserUtils rsqlParserUtils;

    @InjectMock
    NotificationService notificationService;

    @InjectMock
    CleaningRequestService cleaningRequestService;

    @Inject
    RegularOrderService regularOrderService;

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
                    RegularOrderEntity regularOrderEntity = (RegularOrderEntity) invocation.getArguments()[0];
                    regularOrderEntity.setId(UUID.randomUUID());

                    return null;
                }
        ).when(regularOrderRepository).persist((RegularOrderEntity) Mockito.any());

        RegularOrderDto orderToCreate = new RegularOrderDto();
        orderToCreate.setCustomerName(CUSTOMER_NAME);
        orderToCreate.setTargetName(TARGET_NAME);
        orderToCreate.setType(OrderType.REGULAR);
        orderToCreate.setPrice(1);

        RegularOrderDto createdOrder = regularOrderService.createRegularOrder(orderToCreate);
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
        Mockito.doAnswer(invocation -> {
                    RegularOrderEntity regularOrderEntity = (RegularOrderEntity) invocation.getArguments()[0];
                    regularOrderEntity.setId(UUID.randomUUID());

                    return null;
                }
        ).when(regularOrderRepository).persist((RegularOrderEntity) Mockito.any());

        RegularOrderDto orderToCreate = new RegularOrderDto();
        orderToCreate.setType(OrderType.REGULAR);

        assertThrows(ValidationException.class, () -> regularOrderService.createRegularOrder(orderToCreate));

        orderToCreate.setCustomerName(CUSTOMER_NAME);
        assertThrows(ValidationException.class, () -> regularOrderService.createRegularOrder(orderToCreate));

        orderToCreate.setTargetName(TARGET_NAME);
        assertThrows(ValidationException.class, () -> regularOrderService.createRegularOrder(orderToCreate));

        orderToCreate.setPrice(-1);
        assertThrows(ValidationException.class, () -> regularOrderService.createRegularOrder(orderToCreate));

        orderToCreate.setPrice(0);
        assertThrows(ValidationException.class, () -> regularOrderService.createRegularOrder(orderToCreate));

        orderToCreate.setPrice(1);
        assertDoesNotThrow(() -> regularOrderService.createRegularOrder(orderToCreate));
    }

    @Test
    @TestSecurity(user = TEST_KILLER_NAME, roles = {ApiConstant.ROLE_KILLER})
    void testCreateRegularOrderApplication() {
        UUID orderId = UUID.randomUUID();

        RegularOrderEntity regularOrderEntity = new RegularOrderEntity();
        regularOrderEntity.setId(orderId);
        regularOrderEntity.setStatus(OrderStatus.AWAITING_APPLICATIONS);
        regularOrderEntity.setType(OrderType.REGULAR);

        Mockito.when(regularOrderRepository.findById(orderId)).thenReturn(regularOrderEntity);

        regularOrderService.createRegularOrderApplication(orderId);
    }

    @Test
    @TestSecurity(user = TEST_KILLER_NAME, roles = {ApiConstant.ROLE_KILLER})
    void testCreateRegularOrderApplicationPayloadValidations() {
        UUID orderId = UUID.randomUUID();

        RegularOrderEntity regularOrderEntity = new RegularOrderEntity();
        regularOrderEntity.setId(orderId);
        regularOrderEntity.setType(OrderType.REGULAR);

        Mockito.when(regularOrderRepository.findById(Mockito.any())).thenReturn(null);

        // no order found
        assertThrows(EntityNotFoundByIdException.class, () -> regularOrderService.createRegularOrderApplication(UUID.randomUUID()));

        // incorrect order status
        regularOrderEntity.setStatus(OrderStatus.AWAITING_ASSIGNEE);
        Mockito.when(regularOrderRepository.findById(orderId)).thenReturn(regularOrderEntity);
        assertThrows(ValidationException.class, () -> regularOrderService.createRegularOrderApplication(orderId));
    }

    @Test
    @TestSecurity(user = TEST_KILLER_NAME, roles = {ApiConstant.ROLE_KILLER})
    void testCreateRegularOrderApplicationAlreadyExists() {
        UUID orderId = UUID.randomUUID();

        RegularOrderEntity regularOrderEntity = new RegularOrderEntity();
        regularOrderEntity.setId(orderId);
        regularOrderEntity.setStatus(OrderStatus.AWAITING_APPLICATIONS);
        regularOrderEntity.setType(OrderType.REGULAR);

        Mockito.when(regularOrderRepository.findById(orderId)).thenReturn(regularOrderEntity);

        RegularOrderApplicationEntity existingApplication = new RegularOrderApplicationEntity();
        existingApplication.setId(UUID.randomUUID());
        existingApplication.setRegularOrder(regularOrderEntity);
        regularOrderEntity.setApplications(Set.of(existingApplication));

        Mockito.when(regularOrderApplicationRepository.findByOrderAndUser(Mockito.eq(regularOrderEntity), Mockito.any())).thenReturn(existingApplication);

        RegularOrderApplicationDto createdApplication = regularOrderService.createRegularOrderApplication(orderId);
        assertEquals(createdApplication.getId(), existingApplication.getId());
    }

    @Test
    @TestSecurity(user = TEST_ADMIN_NAME, roles = {ApiConstant.ROLE_ADMIN})
    void testCreateRegularOrderApplicationAsAdmin() {
        UUID orderId = UUID.randomUUID();

        RegularOrderEntity regularOrderEntity = new RegularOrderEntity();
        regularOrderEntity.setId(orderId);
        regularOrderEntity.setStatus(OrderStatus.AWAITING_APPLICATIONS);
        regularOrderEntity.setType(OrderType.REGULAR);

        Mockito.when(regularOrderRepository.findById(orderId)).thenReturn(regularOrderEntity);

        assertThrows(ValidationException.class, () -> regularOrderService.createRegularOrderApplication(orderId));
    }

    @Test
    @TestSecurity(user = TEST_ADMIN_NAME, roles = {ApiConstant.ROLE_ADMIN})
    void testUpdateRegularOrderAssignee() {
        UUID orderId = UUID.randomUUID();
        UUID selectedUserId = testKillerEntity.getId();

        RegularOrderEntity regularOrderEntity = new RegularOrderEntity();
        regularOrderEntity.setId(orderId);
        regularOrderEntity.setStatus(OrderStatus.AWAITING_APPLICATIONS);
        regularOrderEntity.setType(OrderType.REGULAR);
        regularOrderEntity.setApplications(new HashSet<>());

        RegularOrderApplicationEntity regularOrderApplicationEntity = new RegularOrderApplicationEntity();
        regularOrderApplicationEntity.setRegularOrder(regularOrderEntity);
        regularOrderApplicationEntity.setKiller(testKillerEntity);

        regularOrderEntity.getApplications().add(regularOrderApplicationEntity);

        Mockito.when(regularOrderRepository.findById(orderId)).thenReturn(regularOrderEntity);
        Mockito.when(userRepository.findById(selectedUserId)).thenReturn(testKillerEntity);

        RegularOrderDto orderToUpdate = new RegularOrderDto();
        orderToUpdate.setId(orderId);
        orderToUpdate.setAssignee(UserDto.builder().id(testKillerEntity.getId()).build());
        RegularOrderDto updatedOrder = regularOrderService.updateRegularOrder(orderToUpdate);

        // test order updated
        assertEquals(updatedOrder.getStatus(), OrderStatus.AWAITING_ASSIGNEE);
        assertTrue(CollectionUtils.isEmpty(updatedOrder.getApplications()));
        assertEquals(updatedOrder.getAssignee().getId(), testKillerEntity.getId());

        // test notification service called
        Mockito.verify(notificationService, Mockito.times(1)).sendNotificationToUser(
                Mockito.eq(testKillerEntity),
                Mockito.anyString(),
                Mockito.anyString()
        );
    }

    @Test
    @TestSecurity(user = TEST_KILLER_NAME, roles = {ApiConstant.ROLE_KILLER})
    void testUpdateRegularStatusToAwaitingSuit() {
        UUID orderId = UUID.randomUUID();

        RegularOrderEntity regularOrderEntity = new RegularOrderEntity();
        regularOrderEntity.setId(orderId);
        regularOrderEntity.setType(OrderType.REGULAR);
        regularOrderEntity.setAssignee(testKillerEntity);

        Mockito.when(regularOrderRepository.findById(orderId)).thenReturn(regularOrderEntity);

        RegularOrderDto orderToUpdate = new RegularOrderDto();
        orderToUpdate.setId(orderId);

        // ok case
        regularOrderEntity.setStatus(OrderStatus.AWAITING_ASSIGNEE);
        orderToUpdate.setStatus(OrderStatus.AWAITING_SUIT);
        RegularOrderDto updatedOrder = regularOrderService.updateRegularOrder(orderToUpdate);
        assertEquals(updatedOrder.getStatus(), OrderStatus.AWAITING_SUIT);

        // wrong status case
        regularOrderEntity.setStatus(OrderStatus.AWAITING_APPLICATIONS);
        orderToUpdate.setStatus(OrderStatus.AWAITING_SUIT);
        assertThrows(ValidationException.class, () -> regularOrderService.updateRegularOrder(orderToUpdate));
    }

    @Test
    @TestSecurity(user = TEST_KILLER_NAME, roles = {ApiConstant.ROLE_KILLER})
    void testUpdateRegularStatusToAwaitingDegustation() {
        UUID orderId = UUID.randomUUID();

        RegularOrderEntity regularOrderEntity = new RegularOrderEntity();
        regularOrderEntity.setId(orderId);
        regularOrderEntity.setType(OrderType.REGULAR);
        regularOrderEntity.setAssignee(testKillerEntity);

        Mockito.when(regularOrderRepository.findById(orderId)).thenReturn(regularOrderEntity);

        RegularOrderDto orderToUpdate = new RegularOrderDto();
        orderToUpdate.setId(orderId);

        // ok case
        regularOrderEntity.setStatus(OrderStatus.AWAITING_SUIT);
        orderToUpdate.setStatus(OrderStatus.AWAITING_DEGUSTATION);
        RegularOrderDto updatedOrder = regularOrderService.updateRegularOrder(orderToUpdate);
        assertEquals(updatedOrder.getStatus(), OrderStatus.AWAITING_DEGUSTATION);

        // wrong status case
        regularOrderEntity.setStatus(OrderStatus.AWAITING_APPLICATIONS);
        orderToUpdate.setStatus(OrderStatus.AWAITING_DEGUSTATION);
        assertThrows(ValidationException.class, () -> regularOrderService.updateRegularOrder(orderToUpdate));
    }

    @Test
    @TestSecurity(user = TEST_KILLER_NAME, roles = {ApiConstant.ROLE_KILLER})
    void testUpdateRegularStatusToAwaitingSubmission() {
        UUID orderId = UUID.randomUUID();

        RegularOrderEntity regularOrderEntity = new RegularOrderEntity();
        regularOrderEntity.setId(orderId);
        regularOrderEntity.setType(OrderType.REGULAR);
        regularOrderEntity.setAssignee(testKillerEntity);

        Mockito.when(regularOrderRepository.findById(orderId)).thenReturn(regularOrderEntity);

        RegularOrderDto orderToUpdate = new RegularOrderDto();
        orderToUpdate.setId(orderId);

        // ok case
        regularOrderEntity.setStatus(OrderStatus.AWAITING_DEGUSTATION);
        orderToUpdate.setStatus(OrderStatus.AWAITING_SUBMISSION);
        RegularOrderDto updatedOrder = regularOrderService.updateRegularOrder(orderToUpdate);
        assertEquals(updatedOrder.getStatus(), OrderStatus.AWAITING_SUBMISSION);

        // wrong status case
        regularOrderEntity.setStatus(OrderStatus.AWAITING_APPLICATIONS);
        orderToUpdate.setStatus(OrderStatus.AWAITING_SUBMISSION);
        assertThrows(ValidationException.class, () -> regularOrderService.updateRegularOrder(orderToUpdate));
    }

    @Test
    @TestSecurity(user = TEST_KILLER_NAME, roles = {ApiConstant.ROLE_KILLER})
    void testUpdateRegularStatusToAwaitingCleaning() {
        UUID orderId = UUID.randomUUID();

        RegularOrderEntity regularOrderEntity = new RegularOrderEntity();
        regularOrderEntity.setId(orderId);
        regularOrderEntity.setType(OrderType.REGULAR);
        regularOrderEntity.setAssignee(testKillerEntity);

        Mockito.when(regularOrderRepository.findById(orderId)).thenReturn(regularOrderEntity);

        RegularOrderDto orderToUpdate = new RegularOrderDto();
        orderToUpdate.setId(orderId);

        // ok case
        regularOrderEntity.setStatus(OrderStatus.AWAITING_SUBMISSION);
        orderToUpdate.setStatus(OrderStatus.AWAITING_CLEANING);
        RegularOrderDto updatedOrder = regularOrderService.updateRegularOrder(orderToUpdate);
        assertEquals(updatedOrder.getStatus(), OrderStatus.AWAITING_CLEANING);

        ArgumentCaptor<RegularOrderEntity> orderCaptor = ArgumentCaptor.forClass(RegularOrderEntity.class);
        ArgumentCaptor<UserEntity> userCaptor = ArgumentCaptor.forClass(UserEntity.class);
        Mockito.verify(cleaningRequestService).createCleaningForOrder(orderCaptor.capture(), userCaptor.capture());

        assertEquals(orderCaptor.getValue(), regularOrderEntity);
        assertEquals(userCaptor.getValue(), testKillerEntity);

        // wrong status case
        regularOrderEntity.setStatus(OrderStatus.AWAITING_APPLICATIONS);
        orderToUpdate.setStatus(OrderStatus.AWAITING_CLEANING);
        assertThrows(ValidationException.class, () -> regularOrderService.updateRegularOrder(orderToUpdate));
    }

    @Test
    @TestSecurity(user = TEST_ADMIN_NAME, roles = {ApiConstant.ROLE_ADMIN})
    void testUpdateRegularStatusToCompleted() {
        UUID orderId = UUID.randomUUID();

        RegularOrderEntity regularOrderEntity = new RegularOrderEntity();
        regularOrderEntity.setId(orderId);
        regularOrderEntity.setType(OrderType.REGULAR);
        regularOrderEntity.setAssignee(testKillerEntity);

        Mockito.when(regularOrderRepository.findById(orderId)).thenReturn(regularOrderEntity);

        RegularOrderDto orderToUpdate = new RegularOrderDto();
        orderToUpdate.setId(orderId);

        // ok case
        regularOrderEntity.setStatus(OrderStatus.AWAITING_APPROVAL);
        orderToUpdate.setStatus(OrderStatus.COMPLETED);
        RegularOrderDto updatedOrder = regularOrderService.updateRegularOrder(orderToUpdate);
        assertEquals(updatedOrder.getStatus(), OrderStatus.COMPLETED);

        // test notification service called
        Mockito.verify(notificationService, Mockito.times(1)).sendNotificationToUser(
                Mockito.eq(testKillerEntity),
                Mockito.anyString(),
                Mockito.anyString()
        );

        // wrong status case
        regularOrderEntity.setStatus(OrderStatus.AWAITING_APPLICATIONS);
        orderToUpdate.setStatus(OrderStatus.COMPLETED);
        assertThrows(ValidationException.class, () -> regularOrderService.updateRegularOrder(orderToUpdate));
    }
}