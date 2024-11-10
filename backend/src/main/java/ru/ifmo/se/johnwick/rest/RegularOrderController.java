package ru.ifmo.se.johnwick.rest;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import ru.ifmo.se.johnwick.constant.ApiConstant;
import ru.ifmo.se.johnwick.model.dto.OrderDto;
import ru.ifmo.se.johnwick.model.dto.RegularOrderDto;
import ru.ifmo.se.johnwick.model.input.OrderInput;
import ru.ifmo.se.johnwick.model.input.RegularOrderInput;
import ru.ifmo.se.johnwick.service.OrderService;
import ru.ifmo.se.johnwick.service.RegularOrderService;

@Path(ApiConstant.API_V1 + "/order"+"/regular" )
@RolesAllowed("ADMIN")
public class RegularOrderController {

    @Inject
    RegularOrderService regularOrderService;

    @POST
    @Transactional
    public RegularOrderDto createOrder(RegularOrderInput regularOrderInput) {
        return regularOrderService.createRegularOrder(regularOrderInput);
    }
}
