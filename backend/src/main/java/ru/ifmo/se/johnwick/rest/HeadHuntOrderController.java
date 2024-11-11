package ru.ifmo.se.johnwick.rest;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import ru.ifmo.se.johnwick.constant.ApiConstant;
import ru.ifmo.se.johnwick.model.dto.HeadHuntOrderDto;
import ru.ifmo.se.johnwick.model.input.HeadHuntOrderInput;
import ru.ifmo.se.johnwick.service.HeadHuntOrderService;


import java.util.Collection;

//TODO ПРОТЕСТИРОВАТЬ И ПЕРЕПИСАТЬ
@Path(ApiConstant.API_V1 + "/order"+"/head-hunt")
@RolesAllowed("ADMIN")
public class HeadHuntOrderController {
    
        @Inject
        HeadHuntOrderService headHuntOrderService;

        @POST
        @Transactional
        public HeadHuntOrderDto createOrder(HeadHuntOrderInput headHuntOrderInput) {
            return headHuntOrderService.createHeadHuntOrder(headHuntOrderInput);
        }

        @GET
        public Collection<HeadHuntOrderDto> getAllHeadHuntOrders() {
            return headHuntOrderService.getAllHeadHuntOrders();
        }
}
