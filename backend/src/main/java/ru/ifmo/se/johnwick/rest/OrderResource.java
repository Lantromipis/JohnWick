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
import ru.ifmo.se.johnwick.model.input.HeadHuntOrderInput;
import ru.ifmo.se.johnwick.model.input.PromissoryNoteOrderInput;
import ru.ifmo.se.johnwick.model.dto.OrderApplicationDto;
import ru.ifmo.se.johnwick.model.dto.OrderDto;
import ru.ifmo.se.johnwick.model.input.OrderInput;
import ru.ifmo.se.johnwick.model.OrderType;
import ru.ifmo.se.johnwick.model.input.RegularOrderInput;

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

        Collection<OrderEntity> promissoryNotes = OrderEntity.findPromissoryNotesByDebtor(userEntity);
        return orderMapper.entitiesToDtos(promissoryNotes);
    }

    @GET
    @Path("/available")
    @RolesAllowed("KILLER")
    public Collection<OrderDto> getAvailableOrders() {
        Collection<OrderEntity> availableOrders = OrderEntity.findAvailableOrders();
        return orderMapper.entitiesToDtos(availableOrders);
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
        OrderEntity entity = switch (orderInput.getType()) {
            case REGULAR -> orderMapper.mapInputToEntity((RegularOrderInput) orderInput);
            case PROMISSORY_NOTE -> orderMapper.mapInputToEntity((PromissoryNoteOrderInput) orderInput);
            case HEAD_HUNT -> orderMapper.mapInputToEntity((HeadHuntOrderInput) orderInput);
        };

        entity.persist();
        return orderMapper.entityToDto(entity);
    }
}
