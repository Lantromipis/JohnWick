package ru.ifmo.se.johnwick.service.impl;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import ru.ifmo.se.johnwick.exception.ValidationException;
import ru.ifmo.se.johnwick.model.dto.HeadHuntOrderDto;
import ru.ifmo.se.johnwick.model.dto.OrderDto;
import ru.ifmo.se.johnwick.model.dto.PromissoryNoteOrderDto;
import ru.ifmo.se.johnwick.model.dto.RegularOrderDto;
import ru.ifmo.se.johnwick.service.api.HeadHuntOrderService;
import ru.ifmo.se.johnwick.service.api.OrderService;
import ru.ifmo.se.johnwick.service.api.PromissoryNoteOrderService;
import ru.ifmo.se.johnwick.service.api.RegularOrderService;

@ApplicationScoped
public class OrderServiceImpl implements OrderService {

    @Inject
    RegularOrderService regularOrderService;

    @Inject
    HeadHuntOrderService headHuntOrderService;

    @Inject
    PromissoryNoteOrderService promissoryNoteOrderService;

    @Override
    public OrderDto createOrder(OrderDto orderDto) {
        if (orderDto instanceof RegularOrderDto regularOrderDto) {
            return regularOrderService.createRegularOrder(regularOrderDto);
        } else if (orderDto instanceof HeadHuntOrderDto headHuntOrderDto) {
            return headHuntOrderService.createHeadHuntOrder(headHuntOrderDto);
        } else if (orderDto instanceof PromissoryNoteOrderDto promissoryNoteOrderDto) {
            return promissoryNoteOrderService.createPromissoryNoteOrder(promissoryNoteOrderDto);
        } else {
            throw new ValidationException("Unknown order type");
        }
    }

    @Override
    public OrderDto updateOrder(OrderDto orderDto) {
        if (orderDto instanceof RegularOrderDto regularOrderDto) {
            return regularOrderService.updateRegularOrder(regularOrderDto);
        } else if (orderDto instanceof HeadHuntOrderDto headHuntOrderDto) {
            return headHuntOrderService.updateHeadHuntOrder(headHuntOrderDto);
        } else if (orderDto instanceof PromissoryNoteOrderDto promissoryNoteOrderDto) {
            return promissoryNoteOrderService.updatePromissoryNoteOrder(promissoryNoteOrderDto);
        } else {
            throw new ValidationException("Unknown order type");
        }
    }
}
