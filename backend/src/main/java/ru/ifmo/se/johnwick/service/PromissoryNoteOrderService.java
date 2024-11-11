package ru.ifmo.se.johnwick.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import ru.ifmo.se.johnwick.entity.PromissoryNoteOrderEntity;
import ru.ifmo.se.johnwick.mapper.PromissoryNoteOrderMapper;
import ru.ifmo.se.johnwick.model.dto.PromissoryNoteOrderDto;
import ru.ifmo.se.johnwick.model.input.PromissoryNoteOrderInput;
import ru.ifmo.se.johnwick.repository.PromissoryNoteOrderRepository;


import java.util.Collection;

@ApplicationScoped
public class PromissoryNoteOrderService {
    @Inject
    EntityManager entityManager;

    @Inject
    PromissoryNoteOrderMapper promissoryNoteOrderMapper;

    @Inject
    PromissoryNoteOrderRepository promissoryNoteOrderRepository;


    public PromissoryNoteOrderDto createPromissoryNoteOrder(PromissoryNoteOrderInput promissoryNoteOrderInput) {
        PromissoryNoteOrderEntity entity = promissoryNoteOrderMapper.mapInputToEntity(promissoryNoteOrderInput);;
        entityManager.persist(entity);
        return promissoryNoteOrderMapper.mapEntityToDto(entity);
    }
    public Collection<PromissoryNoteOrderDto> getAllPromissoryNoteOrders() {
        Collection<PromissoryNoteOrderEntity> entityCollection = promissoryNoteOrderRepository.findAll().list();
        return promissoryNoteOrderMapper.mapEntitiesToDtos(entityCollection);
    }
}
