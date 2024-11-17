package ru.ifmo.se.johnwick.rest.controller;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import ru.ifmo.se.johnwick.constant.ApiConstant;
import ru.ifmo.se.johnwick.model.dto.*;
import ru.ifmo.se.johnwick.service.api.HeadHuntOrderService;
import ru.ifmo.se.johnwick.service.api.OrderService;
import ru.ifmo.se.johnwick.service.api.PromissoryNoteOrderService;
import ru.ifmo.se.johnwick.service.api.RegularOrderService;

import java.util.List;
import java.util.UUID;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path(ApiConstant.API_V1 + "/order")
public class OrderResource {

    @Inject
    OrderService orderService;

    @Inject
    RegularOrderService regularOrderService;

    @Inject
    HeadHuntOrderService headHuntOrderService;

    @Inject
    PromissoryNoteOrderService promissoryNoteOrderService;

    @POST
    public OrderDto createOrder(OrderDto order) {
        return orderService.createOrder(order);
    }

    @PATCH
    @Path("/{id}")
    public OrderDto updateOrder(@PathParam("id") UUID orderId, OrderDto order) {
        order.setId(orderId);
        return orderService.updateOrder(order);
    }

    @GET
    @Path("/regular/{id}")
    @RolesAllowed({ApiConstant.ROLE_KILLER, ApiConstant.ROLE_ADMIN})
    public RegularOrderDto getRegularOrder(@PathParam("id") UUID orderId) {
        return regularOrderService.getRegularOrder(orderId);
    }

    @GET
    @Path("/regular")
    @RolesAllowed({ApiConstant.ROLE_KILLER, ApiConstant.ROLE_ADMIN})
    public List<RegularOrderDto> listRegularOrders(@QueryParam("rsqlPredicate") String rsqlPredicate) {
        return regularOrderService.listRegularOrders(rsqlPredicate);
    }

    @GET
    @Path("/regular/applications")
    @RolesAllowed(ApiConstant.ROLE_KILLER)
    public List<RegularOrderApplicationDto> listRegularOrderApplications(@QueryParam("rsqlPredicate") String rsqlPredicate) {
        return regularOrderService.listRegularOrderApplications(rsqlPredicate);
    }

    @POST
    @Path("/regular/{id}/applications")
    @RolesAllowed(ApiConstant.ROLE_KILLER)
    public RegularOrderApplicationDto createRegularOrderApplication(@PathParam("id") UUID orderId) {
        return regularOrderService.createRegularOrderApplication(orderId);
    }

    @GET
    @Path("/promissoryNote")
    @RolesAllowed({ApiConstant.ROLE_KILLER, ApiConstant.ROLE_ADMIN})
    public List<PromissoryNoteOrderDto> listPromissoryNoteOrders(@QueryParam("rsqlPredicate") String rsqlPredicate) {
        return promissoryNoteOrderService.listPromissoryNoteOrders(rsqlPredicate);
    }

    @GET
    @Path("/headHunt")
    @RolesAllowed({ApiConstant.ROLE_KILLER, ApiConstant.ROLE_ADMIN})
    public List<HeadHuntOrderDto> listHeadHauntOrders(@QueryParam("rsqlPredicate") String rsqlPredicate) {
        return headHuntOrderService.listHeadHuntOrders(rsqlPredicate);
    }
}
