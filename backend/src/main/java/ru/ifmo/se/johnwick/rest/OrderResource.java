package ru.ifmo.se.johnwick.rest;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.SecurityContext;
import ru.ifmo.se.johnwick.entity.OrderEntity;
import ru.ifmo.se.johnwick.entity.UserEntity;
import ru.ifmo.se.johnwick.mapper.OrderMapper;
import ru.ifmo.se.johnwick.model.BillOrderInput;
import ru.ifmo.se.johnwick.model.Order;
import ru.ifmo.se.johnwick.model.OrderInput;
import ru.ifmo.se.johnwick.model.TargetOrderInput;

import java.util.Collection;

@Path("/order")
@RolesAllowed("ADMIN")
public class OrderResource {
    @Inject
    OrderMapper orderMapper;

    @GET
    public Collection<Order> getAllOrders() {
        return orderMapper.entitiesToDtos(OrderEntity.findAll().list());
    }

    @GET
    @Path("/my")
    @RolesAllowed("KILLER")
    public Collection<Order> getMyOrders(@Context SecurityContext sec) {
        String username = sec.getUserPrincipal().getName();
        UserEntity userEntity = UserEntity.findByUsername(username);
        Collection<OrderEntity> bills = OrderEntity.findBillsByDebtor(userEntity);
        return orderMapper.entitiesToDtos(bills);
    }

    @GET
    @Path("/available")
    @RolesAllowed("KILLER")
    public Collection<Order> getAvailableOrders(@Context SecurityContext sec) {
        String username = sec.getUserPrincipal().getName();
        UserEntity userEntity = UserEntity.findByUsername(username);
        Collection<OrderEntity> bills = OrderEntity.findAvailableOrders();
        return orderMapper.entitiesToDtos(bills);
    }

    @POST
    @Transactional
    public Order createOrder(OrderInput orderInput) {
        OrderEntity entity;
        if (orderInput instanceof TargetOrderInput) {
            entity = orderMapper.mapInputToEntity((TargetOrderInput)orderInput);
        } else if (orderInput instanceof BillOrderInput) {
            entity = orderMapper.mapInputToEntity((BillOrderInput)orderInput);
        } else {
            throw new IllegalStateException();
        }

        entity.persist();
        return orderMapper.entityToDto(entity);
    }
}
