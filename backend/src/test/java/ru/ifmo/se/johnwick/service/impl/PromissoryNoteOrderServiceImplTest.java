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
import ru.ifmo.se.johnwick.exception.EntityNotFoundByIdException;
import ru.ifmo.se.johnwick.exception.ValidationException;
import ru.ifmo.se.johnwick.model.OrderStatus;
import ru.ifmo.se.johnwick.model.OrderType;
import ru.ifmo.se.johnwick.model.UserRole;
import ru.ifmo.se.johnwick.model.dto.PromissoryNoteOrderDto;
import ru.ifmo.se.johnwick.model.dto.UserDto;
import ru.ifmo.se.johnwick.model.entity.PromissoryNoteOrderEntity;
import ru.ifmo.se.johnwick.model.entity.UserEntity;
import ru.ifmo.se.johnwick.repository.PromissoryNoteOrderRepository;
import ru.ifmo.se.johnwick.repository.UserRepository;
import ru.ifmo.se.johnwick.service.api.CleaningRequestService;
import ru.ifmo.se.johnwick.service.api.NotificationService;
import ru.ifmo.se.johnwick.service.api.PromissoryNoteOrderService;
import ru.ifmo.se.johnwick.utils.RsqlParserUtils;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@QuarkusTest
class PromissoryNoteOrderServiceImplTest {

    @InjectMock
    PromissoryNoteOrderRepository promissoryNoteOrderRepository;

    @InjectMock
    RsqlParserUtils rsqlParserUtils;

    @InjectMock
    NotificationService notificationService;

    @InjectMock
    UserRepository userRepository;

    @InjectMock
    CleaningRequestService cleaningRequestService;

    @Inject
    PromissoryNoteOrderService promissoryNoteOrderService;

    public static final String TEST_KILLER_NAME = "testKiller";
    public static final String TEST_TAILOR_NAME = "testTailor";
    public static final String TEST_ADMIN_NAME = "testAdmin";

    private UserEntity testKillerEntity;
    private UserEntity testTailorEntity;
    private UserEntity testAdminEntity;

    @BeforeEach
    void setUp() {
        testKillerEntity = new UserEntity();
        testKillerEntity.setUsername(TEST_KILLER_NAME);
        testKillerEntity.setId(UUID.randomUUID());
        testKillerEntity.setRole(UserRole.KILLER);

        Mockito.when(userRepository.findByUsername(TEST_KILLER_NAME)).thenReturn(testKillerEntity);
        Mockito.when(userRepository.findById(testKillerEntity.getId())).thenReturn(testKillerEntity);

        testTailorEntity = new UserEntity();
        testTailorEntity.setUsername(TEST_TAILOR_NAME);
        testTailorEntity.setId(UUID.randomUUID());
        testTailorEntity.setRole(UserRole.TAILOR);

        Mockito.when(userRepository.findByUsername(TEST_TAILOR_NAME)).thenReturn(testTailorEntity);
        Mockito.when(userRepository.findById(testTailorEntity.getId())).thenReturn(testTailorEntity);

        testAdminEntity = new UserEntity();
        testAdminEntity.setUsername(TEST_ADMIN_NAME);
        testAdminEntity.setId(UUID.randomUUID());
        testAdminEntity.setRole(UserRole.ADMIN);

        Mockito.when(userRepository.findByUsername(TEST_ADMIN_NAME)).thenReturn(testAdminEntity);
        Mockito.when(userRepository.findById(testAdminEntity.getId())).thenReturn(testAdminEntity);
    }

    @Test
    void testCreatePromissoryNoteOrder() {
        PromissoryNoteOrderDto orderToCreate = new PromissoryNoteOrderDto();
        orderToCreate.setType(OrderType.PROMISSORY_NOTE);
        orderToCreate.setDebtor(UserDto.builder().id(testKillerEntity.getId()).build());
        orderToCreate.setBeneficiary(UserDto.builder().id(testTailorEntity.getId()).build());

        Mockito.doAnswer(invocation -> {
                    PromissoryNoteOrderEntity promissoryNoteOrderEntity = (PromissoryNoteOrderEntity) invocation.getArguments()[0];
                    promissoryNoteOrderEntity.setId(UUID.randomUUID());

                    return null;
                }
        ).when(promissoryNoteOrderRepository).persist((PromissoryNoteOrderEntity) Mockito.any());

        PromissoryNoteOrderDto createdOrder = promissoryNoteOrderService.createPromissoryNoteOrder(orderToCreate);
        assertEquals(createdOrder.getDebtor().getId(), testKillerEntity.getId());
        assertEquals(createdOrder.getBeneficiary().getId(), testTailorEntity.getId());

        // test notification service called
        Mockito.verify(notificationService, Mockito.times(1)).sendNotificationToUser(
                Mockito.eq(testKillerEntity),
                Mockito.anyString(),
                Mockito.anyString()
        );
    }

    @Test
    void testCreatePromissoryNoteOrderValidations() {
        PromissoryNoteOrderDto orderToCreate = new PromissoryNoteOrderDto();
        orderToCreate.setType(OrderType.PROMISSORY_NOTE);

        // test no debtor
        assertThrows(ValidationException.class, () -> promissoryNoteOrderService.createPromissoryNoteOrder(orderToCreate));

        // test bo beneficiary
        orderToCreate.setDebtor(UserDto.builder().id(UUID.randomUUID()).build());
        assertThrows(ValidationException.class, () -> promissoryNoteOrderService.createPromissoryNoteOrder(orderToCreate));

        // test unknown debtor
        orderToCreate.setDebtor(UserDto.builder().id(UUID.randomUUID()).build());
        orderToCreate.setBeneficiary(UserDto.builder().id(testTailorEntity.getId()).build());
        assertThrows(EntityNotFoundByIdException.class, () -> promissoryNoteOrderService.createPromissoryNoteOrder(orderToCreate));

        // test unknown beneficiary
        orderToCreate.setDebtor(UserDto.builder().id(testKillerEntity.getId()).build());
        orderToCreate.setBeneficiary(UserDto.builder().id(UUID.randomUUID()).build());
        assertThrows(EntityNotFoundByIdException.class, () -> promissoryNoteOrderService.createPromissoryNoteOrder(orderToCreate));
    }

    @Test
    @TestSecurity(user = TEST_KILLER_NAME, roles = {ApiConstant.ROLE_KILLER})
    void testUpdatePromissoryNoteStatusToAwaitingSuit() {
        UUID orderId = UUID.randomUUID();

        PromissoryNoteOrderEntity promissoryNoteOrderEntity = new PromissoryNoteOrderEntity();
        promissoryNoteOrderEntity.setId(orderId);
        promissoryNoteOrderEntity.setType(OrderType.PROMISSORY_NOTE);
        promissoryNoteOrderEntity.setDebtor(testKillerEntity);

        Mockito.when(promissoryNoteOrderRepository.findById(orderId)).thenReturn(promissoryNoteOrderEntity);

        PromissoryNoteOrderDto orderToUpdate = new PromissoryNoteOrderDto();
        orderToUpdate.setId(orderId);

        // ok case
        promissoryNoteOrderEntity.setStatus(OrderStatus.AWAITING_ASSIGNEE);
        orderToUpdate.setStatus(OrderStatus.AWAITING_SUIT);
        PromissoryNoteOrderDto updatedOrder = promissoryNoteOrderService.updatePromissoryNoteOrder(orderToUpdate);
        assertEquals(updatedOrder.getStatus(), OrderStatus.AWAITING_SUIT);

        // wrong status case
        promissoryNoteOrderEntity.setStatus(OrderStatus.AWAITING_APPLICATIONS);
        orderToUpdate.setStatus(OrderStatus.AWAITING_SUIT);
        assertThrows(ValidationException.class, () -> promissoryNoteOrderService.updatePromissoryNoteOrder(orderToUpdate));
    }

    @Test
    @TestSecurity(user = TEST_KILLER_NAME, roles = {ApiConstant.ROLE_KILLER})
    void testUpdatePromissoryNoteStatusToAwaitingDegustation() {
        UUID orderId = UUID.randomUUID();

        PromissoryNoteOrderEntity promissoryNoteOrderEntity = new PromissoryNoteOrderEntity();
        promissoryNoteOrderEntity.setId(orderId);
        promissoryNoteOrderEntity.setType(OrderType.PROMISSORY_NOTE);
        promissoryNoteOrderEntity.setDebtor(testKillerEntity);
        promissoryNoteOrderEntity.setBeneficiary(testTailorEntity);

        Mockito.when(promissoryNoteOrderRepository.findById(orderId)).thenReturn(promissoryNoteOrderEntity);

        PromissoryNoteOrderDto orderToUpdate = new PromissoryNoteOrderDto();
        orderToUpdate.setId(orderId);

        // ok case
        promissoryNoteOrderEntity.setStatus(OrderStatus.AWAITING_SUIT);
        orderToUpdate.setStatus(OrderStatus.AWAITING_DEGUSTATION);
        PromissoryNoteOrderDto updatedOrder = promissoryNoteOrderService.updatePromissoryNoteOrder(orderToUpdate);
        assertEquals(updatedOrder.getStatus(), OrderStatus.AWAITING_DEGUSTATION);

        // wrong status case
        promissoryNoteOrderEntity.setStatus(OrderStatus.AWAITING_APPLICATIONS);
        orderToUpdate.setStatus(OrderStatus.AWAITING_DEGUSTATION);
        assertThrows(ValidationException.class, () -> promissoryNoteOrderService.updatePromissoryNoteOrder(orderToUpdate));
    }

    @Test
    @TestSecurity(user = TEST_KILLER_NAME, roles = {ApiConstant.ROLE_KILLER})
    void testUpdatePromissoryNoteStatusToAwaitingSubmission() {
        UUID orderId = UUID.randomUUID();

        PromissoryNoteOrderEntity promissoryNoteOrderEntity = new PromissoryNoteOrderEntity();
        promissoryNoteOrderEntity.setId(orderId);
        promissoryNoteOrderEntity.setType(OrderType.PROMISSORY_NOTE);
        promissoryNoteOrderEntity.setDebtor(testKillerEntity);
        promissoryNoteOrderEntity.setBeneficiary(testTailorEntity);

        Mockito.when(promissoryNoteOrderRepository.findById(orderId)).thenReturn(promissoryNoteOrderEntity);

        PromissoryNoteOrderDto orderToUpdate = new PromissoryNoteOrderDto();
        orderToUpdate.setId(orderId);

        // ok case
        promissoryNoteOrderEntity.setStatus(OrderStatus.AWAITING_DEGUSTATION);
        orderToUpdate.setStatus(OrderStatus.AWAITING_SUBMISSION);
        PromissoryNoteOrderDto updatedOrder = promissoryNoteOrderService.updatePromissoryNoteOrder(orderToUpdate);
        assertEquals(updatedOrder.getStatus(), OrderStatus.AWAITING_SUBMISSION);

        // wrong status case
        promissoryNoteOrderEntity.setStatus(OrderStatus.AWAITING_APPLICATIONS);
        orderToUpdate.setStatus(OrderStatus.AWAITING_SUBMISSION);
        assertThrows(ValidationException.class, () -> promissoryNoteOrderService.updatePromissoryNoteOrder(orderToUpdate));
    }

    @Test
    @TestSecurity(user = TEST_KILLER_NAME, roles = {ApiConstant.ROLE_KILLER})
    void testUpdatePromissoryNoteStatusToAwaitingCleaning() {
        UUID orderId = UUID.randomUUID();

        PromissoryNoteOrderEntity promissoryNoteOrderEntity = new PromissoryNoteOrderEntity();
        promissoryNoteOrderEntity.setId(orderId);
        promissoryNoteOrderEntity.setType(OrderType.PROMISSORY_NOTE);
        promissoryNoteOrderEntity.setDebtor(testKillerEntity);
        promissoryNoteOrderEntity.setBeneficiary(testTailorEntity);

        Mockito.when(promissoryNoteOrderRepository.findById(orderId)).thenReturn(promissoryNoteOrderEntity);

        PromissoryNoteOrderDto orderToUpdate = new PromissoryNoteOrderDto();
        orderToUpdate.setId(orderId);

        // ok case
        promissoryNoteOrderEntity.setStatus(OrderStatus.AWAITING_SUBMISSION);
        orderToUpdate.setStatus(OrderStatus.AWAITING_CLEANING);
        PromissoryNoteOrderDto updatedOrder = promissoryNoteOrderService.updatePromissoryNoteOrder(orderToUpdate);
        assertEquals(updatedOrder.getStatus(), OrderStatus.AWAITING_CLEANING);

        ArgumentCaptor<PromissoryNoteOrderEntity> orderCaptor = ArgumentCaptor.forClass(PromissoryNoteOrderEntity.class);
        ArgumentCaptor<UserEntity> userCaptor = ArgumentCaptor.forClass(UserEntity.class);
        Mockito.verify(cleaningRequestService).createCleaningForOrder(orderCaptor.capture(), userCaptor.capture());

        assertEquals(orderCaptor.getValue(), promissoryNoteOrderEntity);
        assertEquals(userCaptor.getValue(), testKillerEntity);

        // wrong status case
        promissoryNoteOrderEntity.setStatus(OrderStatus.AWAITING_APPLICATIONS);
        orderToUpdate.setStatus(OrderStatus.AWAITING_CLEANING);
        assertThrows(ValidationException.class, () -> promissoryNoteOrderService.updatePromissoryNoteOrder(orderToUpdate));
    }

    @Test
    @TestSecurity(user = TEST_ADMIN_NAME, roles = {ApiConstant.ROLE_ADMIN})
    void testUpdatePromissoryNoteStatusToCompleted() {
        UUID orderId = UUID.randomUUID();

        PromissoryNoteOrderEntity promissoryNoteOrderEntity = new PromissoryNoteOrderEntity();
        promissoryNoteOrderEntity.setId(orderId);
        promissoryNoteOrderEntity.setType(OrderType.PROMISSORY_NOTE);
        promissoryNoteOrderEntity.setDebtor(testKillerEntity);
        promissoryNoteOrderEntity.setBeneficiary(testTailorEntity);

        Mockito.when(promissoryNoteOrderRepository.findById(orderId)).thenReturn(promissoryNoteOrderEntity);

        PromissoryNoteOrderDto orderToUpdate = new PromissoryNoteOrderDto();
        orderToUpdate.setId(orderId);

        // ok case
        promissoryNoteOrderEntity.setStatus(OrderStatus.AWAITING_APPROVAL);
        orderToUpdate.setStatus(OrderStatus.COMPLETED);
        PromissoryNoteOrderDto updatedOrder = promissoryNoteOrderService.updatePromissoryNoteOrder(orderToUpdate);
        assertEquals(updatedOrder.getStatus(), OrderStatus.COMPLETED);

        // test notification service called
        Mockito.verify(notificationService, Mockito.times(1)).sendNotificationToUser(
                Mockito.eq(testKillerEntity),
                Mockito.anyString(),
                Mockito.anyString()
        );
        Mockito.verify(notificationService, Mockito.times(1)).sendNotificationToUser(
                Mockito.eq(testTailorEntity),
                Mockito.anyString(),
                Mockito.anyString()
        );

        // wrong status case
        promissoryNoteOrderEntity.setStatus(OrderStatus.AWAITING_APPLICATIONS);
        orderToUpdate.setStatus(OrderStatus.COMPLETED);
        assertThrows(ValidationException.class, () -> promissoryNoteOrderService.updatePromissoryNoteOrder(orderToUpdate));
    }
}