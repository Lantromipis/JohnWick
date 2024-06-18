package ru.ifmo.se.johnwick.rest;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.SecurityContext;
import ru.ifmo.se.johnwick.constant.ApiConstant;
import ru.ifmo.se.johnwick.entity.OrderApplicationEntity;
import ru.ifmo.se.johnwick.entity.OrderEntity;
import ru.ifmo.se.johnwick.entity.UserEntity;
import ru.ifmo.se.johnwick.mapper.OrderApplicationMapper;
import ru.ifmo.se.johnwick.mapper.OrderMapper;
import ru.ifmo.se.johnwick.model.BillOrderInput;
import ru.ifmo.se.johnwick.model.OrderApplicationDto;
import ru.ifmo.se.johnwick.model.OrderDto;
import ru.ifmo.se.johnwick.model.OrderInput;
import ru.ifmo.se.johnwick.model.OrderType;
import ru.ifmo.se.johnwick.model.TargetOrderInput;

import java.util.Collection;

@Path(ApiConstant.API_V1 + "/order")
@RolesAllowed("ADMIN")
public class OrderResource {
    @Inject
    OrderMapper orderMapper;

    @Inject
    OrderApplicationMapper orderApplicationMapper;

    @GET
    public Collection<OrderDto> getAllOrders() {
        return orderMapper.entitiesToDtos(OrderEntity.findNotCanceled());
    }

    @GET
    @Path("/my")
    @RolesAllowed("KILLER")
    public Collection<OrderDto> getMyOrders(@Context SecurityContext sec) {
        String username = sec.getUserPrincipal().getName();
        UserEntity userEntity = UserEntity.findByUsername(username);

        Collection<OrderEntity> bills = OrderEntity.findBillsByDebtor(userEntity);
        return orderMapper.entitiesToDtos(bills);
    }

    @GET
    @Path("/available")
    @RolesAllowed("KILLER")
    public Collection<OrderDto> getAvailableOrders() {
        Collection<OrderEntity> bills = OrderEntity.findAvailableOrders();
        return orderMapper.entitiesToDtos(bills);
    }

    @PUT
    @Transactional
    @RolesAllowed("KILLER")
    @Path("/{orderId}/apply")
    public OrderApplicationDto applyForDefaultOrder(@PathParam("orderId") long orderId,
                                                    @Context SecurityContext sec) {
        OrderEntity orderEntity = OrderEntity.findById(orderId);
        if (!orderEntity.getType().equals(OrderType.DEFAULT)) {
            throw new IllegalArgumentException();
        }

        String username = sec.getUserPrincipal().getName();
        UserEntity userEntity = UserEntity.findByUsername(username);

        OrderApplicationEntity orderApplicationEntity = new OrderApplicationEntity(userEntity, orderEntity);
        orderApplicationEntity.persist();

        return orderApplicationMapper.entityToDto(orderApplicationEntity);
    }

    @GET
    @Path("/application")
    public Collection<OrderApplicationDto> getApplications(@QueryParam("orderId") long orderId) {
        OrderEntity orderEntity = OrderEntity.findById(orderId);
        Collection<OrderApplicationEntity> applications = OrderApplicationEntity.findByOrder(orderEntity);
        return orderApplicationMapper.entitiesToDtos(applications);
    }

    @PUT
    @Transactional
    @Path("/{orderId}/cancel")
    public OrderDto cancelOrder(@PathParam("orderId") long orderId) {
        OrderEntity orderEntity = OrderEntity.cancelOrderById(orderId);
        return orderMapper.entityToDto(orderEntity);
    }

    @POST
    @Transactional
    public OrderDto createOrder(OrderInput orderInput) {
        OrderEntity entity;
        if (orderInput instanceof TargetOrderInput) {
            entity = orderMapper.mapInputToEntity((TargetOrderInput) orderInput);
        } else if (orderInput instanceof BillOrderInput) {
            entity = orderMapper.mapInputToEntity((BillOrderInput) orderInput);
        } else {
            throw new IllegalStateException();
        }

        entity.persist();
        return orderMapper.entityToDto(entity);
    }
}
