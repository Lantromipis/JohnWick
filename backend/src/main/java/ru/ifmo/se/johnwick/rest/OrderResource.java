package ru.ifmo.se.johnwick.rest;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.SecurityContext;
import ru.ifmo.se.johnwick.constant.ApiConstant;
import ru.ifmo.se.johnwick.entity.OrderApplicationEntity;
import ru.ifmo.se.johnwick.entity.OrderEntity;
import ru.ifmo.se.johnwick.entity.UserEntity;
import ru.ifmo.se.johnwick.mapper.OrderApplicationMapper;
import ru.ifmo.se.johnwick.mapper.OrderMapper;
import ru.ifmo.se.johnwick.model.OrderType;
import ru.ifmo.se.johnwick.model.dto.AvailableOrderDto;
import ru.ifmo.se.johnwick.model.dto.OrderApplicationDto;
import ru.ifmo.se.johnwick.model.dto.OrderDto;
import ru.ifmo.se.johnwick.model.input.OrderInput;
import ru.ifmo.se.johnwick.repository.OrderApplicationRepository;
import ru.ifmo.se.johnwick.repository.OrderRepository;
import ru.ifmo.se.johnwick.repository.UserRepository;
import ru.ifmo.se.johnwick.service.NotificationService;
import ru.ifmo.se.johnwick.service.OrderService;

import java.util.Collection;

@Path(ApiConstant.API_V1 + "/order")
@RolesAllowed("ADMIN")
public class OrderResource {
    @Inject
    OrderMapper orderMapper;

    @Inject
    OrderService orderService;

    @Inject
    NotificationService notificationService;

    @Inject
    OrderApplicationMapper orderApplicationMapper;

    @Inject
    OrderApplicationRepository orderApplicationRepository;

    @Inject
    OrderRepository orderRepository;

    @Inject
    UserRepository userRepository;

    @GET
    public Collection<OrderDto> getAllOrders() {
        return orderMapper.entitiesToDtos(orderRepository.findNotCanceled());
    }

    @GET
    @Path("/my")
    @RolesAllowed("KILLER")
    public Collection<OrderDto> getMyOrders(@Context SecurityContext sec) {
        String username = sec.getUserPrincipal().getName();
        UserEntity userEntity = userRepository.findByUsername(username);

        Collection<OrderEntity> promissoryNotes = orderRepository.findByAssignee(userEntity);
        return orderMapper.entitiesToDtos(promissoryNotes);
    }

    @GET
    @Path("/available")
    @RolesAllowed("KILLER")
    public Collection<AvailableOrderDto> getAvailableOrders(@Context SecurityContext sec) {
        String username = sec.getUserPrincipal().getName();
        UserEntity userEntity = userRepository.findByUsername(username);

        Collection<OrderEntity> availableOrders = orderRepository.findAvailableOrders();
        return orderMapper.entitiesToAvailableDtos(availableOrders, userEntity);
    }

    @PUT
    @Transactional
    @RolesAllowed("KILLER")
    @Path("/{orderId}/apply")
    public OrderApplicationDto applyForRegularOrder(@PathParam("orderId") long orderId,
                                                    @Context SecurityContext sec) {
        OrderEntity orderEntity = OrderEntity.findById(orderId);
        if (!orderEntity.getType().equals(OrderType.REGULAR)) {
            throw new IllegalArgumentException();
        }

        String username = sec.getUserPrincipal().getName();
        UserEntity userEntity = userRepository.findByUsername(username);

        OrderApplicationEntity orderApplicationEntity = new OrderApplicationEntity(userEntity, orderEntity);
        orderApplicationEntity.persist();

        return orderApplicationMapper.entityToDto(orderApplicationEntity);
    }

    @GET
    @Path("/{orderId}/application")
    public Collection<OrderApplicationDto> getApplications(@PathParam("orderId") long orderId) {
        OrderEntity orderEntity = OrderEntity.findById(orderId);
        Collection<OrderApplicationEntity> applications = orderApplicationRepository.findByOrder(orderEntity);
        return orderApplicationMapper.entitiesToDtos(applications);
    }

    @PUT
    @Transactional
    @Path("/application/{applicationId}/choose")
    public OrderDto chooseApplication(@PathParam("applicationId") long applicationId) {
        OrderApplicationEntity orderApplicationEntity = OrderApplicationEntity.findById(applicationId);
        OrderEntity orderEntity = orderApplicationEntity.getOrder();
        UserEntity userEntity = orderApplicationEntity.getAppliedKiller();

        orderEntity.setAssignee(userEntity);

        notificationService.notifyAboutAcceptedApplication(orderApplicationMapper.entityToDto(orderApplicationEntity));

        return orderMapper.entityToDto(orderEntity);
    }

    @PUT
    @Transactional
    @Path("/{orderId}/cancel")
    public OrderDto cancelOrder(@PathParam("orderId") long orderId) {
        OrderEntity orderEntity = orderRepository.cancelOrderById(orderId);
        return orderMapper.entityToDto(orderEntity);
    }

    @POST
    @Transactional
    public OrderDto createOrder(OrderInput orderInput) {
        OrderEntity entity = orderService.createOrder(orderInput);
        return orderMapper.entityToDto(entity);
    }
}
