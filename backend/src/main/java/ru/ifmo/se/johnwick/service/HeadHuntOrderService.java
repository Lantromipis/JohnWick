package ru.ifmo.se.johnwick.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import ru.ifmo.se.johnwick.entity.HeadHuntOrderEntity;
import ru.ifmo.se.johnwick.mapper.HeadHuntOrderMapper;
import ru.ifmo.se.johnwick.model.dto.HeadHuntOrderDto;
import ru.ifmo.se.johnwick.model.input.HeadHuntOrderInput;
import ru.ifmo.se.johnwick.repository.HeadHuntOrderRepository;


import java.time.Instant;
import java.util.Collection;

@ApplicationScoped
public class HeadHuntOrderService {
    @Inject
    EntityManager entityManager;

    @Inject
    HeadHuntOrderMapper headHuntOrderMapper;

    @Inject
    HeadHuntOrderRepository headHuntOrderRepository;


    public HeadHuntOrderDto createHeadHuntOrder(HeadHuntOrderInput headHuntOrderInput) {
        HeadHuntOrderEntity entity = headHuntOrderMapper.mapInputToEntity(headHuntOrderInput);
        entityManager.persist(entity);
        return headHuntOrderMapper.mapEntityToDto(entity);
    }
    public Collection<HeadHuntOrderDto> getAllHeadHuntOrders() {
        Collection<HeadHuntOrderEntity> entityCollection = headHuntOrderRepository.findAll().list();
        return headHuntOrderMapper.mapEntitiesToDtos(entityCollection);
    }
}
