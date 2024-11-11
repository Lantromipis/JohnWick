package ru.ifmo.se.johnwick.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import ru.ifmo.se.johnwick.entity.OrderEntity;
import ru.ifmo.se.johnwick.mapper.OrderMapper;
import ru.ifmo.se.johnwick.model.dto.OrderDto;
import ru.ifmo.se.johnwick.model.input.OrderInput;
import ru.ifmo.se.johnwick.repository.OrderRepository;
import java.time.Instant;
import java.util.Collection;

@ApplicationScoped
public class OrderService {

    @Inject
    EntityManager entityManager;

    @Inject
    OrderMapper orderMapper;

    @Inject
    OrderRepository orderRepository;


    public OrderDto createOrder(OrderInput orderInput) {
        OrderEntity entity = orderMapper.mapInputToEntity(orderInput);
        entity.setCreatedTimestamp(Instant.now());
        entityManager.persist(entity);
        return orderMapper.mapEntityToDto(entity);
    }
    public Collection<OrderDto> getAllOrders() {
        Collection<OrderEntity> entityCollection = orderRepository.findAll().list();
        return orderMapper.mapEntitiesToDtos(entityCollection);
    }
}
