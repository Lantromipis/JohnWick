package ru.ifmo.se.johnwick.rest;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.SecurityContext;
import ru.ifmo.se.johnwick.constant.ApiConstant;
import ru.ifmo.se.johnwick.model.dto.AvailableOrderDto;
import ru.ifmo.se.johnwick.model.dto.OrderApplicationDto;
import ru.ifmo.se.johnwick.model.dto.OrderDto;
import ru.ifmo.se.johnwick.model.input.OrderInput;
import ru.ifmo.se.johnwick.service.OrderService;

import java.util.Collection;

@Path(ApiConstant.API_V1 + "/order")
@RolesAllowed("ADMIN")
public class OrderResource {
    @Inject
    OrderService orderService;

    @GET
    public Collection<OrderDto> getAllOrders() {
        return orderService.getActiveOrders();
    }

    @GET
    @Path("/my")
    @RolesAllowed("KILLER")
    public Collection<OrderDto> getMyOrders(@Context SecurityContext sec) {
        return orderService.getUserAssignedOrders(sec.getUserPrincipal().getName());
    }

    @GET
    @Path("/available")
    @RolesAllowed("KILLER")
    public Collection<AvailableOrderDto> getAvailableOrders(@Context SecurityContext sec) {
        return orderService.getUserAvailableOrders(sec.getUserPrincipal().getName());
    }

    @PUT
    @Transactional
    @RolesAllowed("KILLER")
    @Path("/{orderId}/apply")
    public OrderApplicationDto applyForRegularOrder(@PathParam("orderId") long orderId,
                                                    @Context SecurityContext sec) {
        return orderService.createRegularOrderApplication(orderId, sec.getUserPrincipal().getName());
    }

    @GET
    @Path("/{orderId}/application")
    public Collection<OrderApplicationDto> getApplications(@PathParam("orderId") long orderId) {
        return orderService.getOrderApplications(orderId);
    }

    @PUT
    @Transactional
    @Path("/application/{applicationId}/choose")
    public OrderDto chooseApplication(@PathParam("applicationId") long applicationId) {
        return orderService.chooseOrderApplication(applicationId);
    }

    @PUT
    @Transactional
    @Path("/{orderId}/cancel")
    public OrderDto cancelOrder(@PathParam("orderId") long orderId) {
        return orderService.cancelOrder(orderId);
    }

    @POST
    @Transactional
    public OrderDto createOrder(OrderInput orderInput) {
        return orderService.createOrder(orderInput);
    }
}
