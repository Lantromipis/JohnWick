package ru.ifmo.se.johnwick.service.api;

import ru.ifmo.se.johnwick.model.dto.HeadHuntOrderDto;

import java.util.List;

public interface HeadHuntOrderService {
    HeadHuntOrderDto createHeadHuntOrder(HeadHuntOrderDto headHuntOrderDto);

    List<HeadHuntOrderDto> listHeadHuntOrders(String rsqlPredicate);
}
