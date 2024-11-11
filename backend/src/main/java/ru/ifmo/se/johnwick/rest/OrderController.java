package ru.ifmo.se.johnwick.rest;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import ru.ifmo.se.johnwick.constant.ApiConstant;
import ru.ifmo.se.johnwick.model.dto.OrderDto;
import ru.ifmo.se.johnwick.model.input.OrderInput;
import ru.ifmo.se.johnwick.service.OrderService;

import java.util.Collection;


//TODO ПРОТЕСТИРОВАТЬ И ПЕРЕПИСАТЬ
@Path(ApiConstant.API_V1 + "/order")
@RolesAllowed("ADMIN")
public class OrderController {
    @Inject
    OrderService orderService;

    @POST
    @Transactional
    public OrderDto createOrder(OrderInput orderInput) {
        return orderService.createOrder(orderInput);
    }

    @GET
    public Collection<OrderDto> getAllOrders() {
        return orderService.getAllOrders();
    }
}
