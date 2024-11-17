package ru.ifmo.se.johnwick.service.api;

import ru.ifmo.se.johnwick.model.dto.PromissoryNoteOrderDto;

import java.util.List;

public interface PromissoryNoteOrderService {
    PromissoryNoteOrderDto createPromissoryNoteOrder(PromissoryNoteOrderDto promissoryNoteOrderDto);

    List<PromissoryNoteOrderDto> listPromissoryNoteOrders(String rsqlPredicate);
}
