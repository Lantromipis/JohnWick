package ru.ifmo.se.johnwick.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import ru.ifmo.se.johnwick.entity.RegularOrderEntity;
import ru.ifmo.se.johnwick.mapper.RegularOrderMapper;
import ru.ifmo.se.johnwick.model.dto.RegularOrderDto;
import ru.ifmo.se.johnwick.model.input.RegularOrderInput;
import ru.ifmo.se.johnwick.repository.RegularOrderRepository;
import java.util.Collection;


@ApplicationScoped
public class RegularOrderService {

    @Inject
    RegularOrderMapper regularOrderMapper;

    @Inject
    EntityManager entityManager;

    @Inject
    RegularOrderRepository regularOrderRepository;

    public RegularOrderDto createRegularOrder(RegularOrderInput regularOrderInput) {
        RegularOrderEntity entity = regularOrderMapper.mapInputToEntity(regularOrderInput);
        entityManager.persist(entity);
        return regularOrderMapper.mapEntityToDto(entity);
    }

    public Collection<RegularOrderDto> getAllRegularOrders() {
        Collection<RegularOrderEntity> entityCollection = regularOrderRepository.findAll().list();
        return regularOrderMapper.mapEntitiesToDtos(entityCollection);
    }
}
