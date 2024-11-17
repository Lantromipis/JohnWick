package ru.ifmo.se.johnwick.service.api;

import ru.ifmo.se.johnwick.model.dto.OrderDto;

public interface OrderService {
    OrderDto createOrder(OrderDto order);

    OrderDto updateOrder(OrderDto order);
}
