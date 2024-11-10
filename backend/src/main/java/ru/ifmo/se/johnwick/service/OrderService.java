package ru.ifmo.se.johnwick.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import ru.ifmo.se.johnwick.entity.OrderEntity;

import ru.ifmo.se.johnwick.mapper.OrderMapper;
import ru.ifmo.se.johnwick.model.dto.OrderDto;
import ru.ifmo.se.johnwick.model.input.OrderInput;

import java.time.Instant;

@ApplicationScoped
public class OrderService {

    @Inject
    OrderMapper orderMapper;

    public OrderDto createOrder(OrderInput orderInput) {
        OrderEntity entity = orderMapper.mapInputToEntity(orderInput);
        entity.setCreatedTimestamp(Instant.now());
        entity.persist();
        return orderMapper.mapEntityToDto(entity);
    }
}
