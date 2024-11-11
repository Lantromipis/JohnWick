package ru.ifmo.se.johnwick.rest;


import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import ru.ifmo.se.johnwick.constant.ApiConstant;
import ru.ifmo.se.johnwick.model.dto.HeadHuntOrderDto;
import ru.ifmo.se.johnwick.model.dto.PromissoryNoteOrderDto;
import ru.ifmo.se.johnwick.model.input.HeadHuntOrderInput;
import ru.ifmo.se.johnwick.model.input.PromissoryNoteOrderInput;
import ru.ifmo.se.johnwick.repository.PromissoryNoteOrderRepository;
import ru.ifmo.se.johnwick.service.HeadHuntOrderService;
import ru.ifmo.se.johnwick.service.PromissoryNoteOrderService;

import java.util.Collection;

//TODO ПРОТЕСТИРОВАТЬ И ПЕРЕПИСАТЬ
@Path(ApiConstant.API_V1 + "/order"+"/premissory-note")
@RolesAllowed("ADMIN")
public class PromissoryNoteOrderController {

    @Inject
    PromissoryNoteOrderService promissoryNoteOrderService;

    @POST
    @Transactional
    public PromissoryNoteOrderDto createOrder(PromissoryNoteOrderInput promissoryNoteOrderInput) {
        return promissoryNoteOrderService.createPromissoryNoteOrder(promissoryNoteOrderInput);
    }

    @GET
    public Collection<PromissoryNoteOrderDto> getAllHeadHuntOrders() {
        return promissoryNoteOrderService.getAllPromissoryNoteOrders();
    }
}
