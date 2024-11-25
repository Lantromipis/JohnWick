package ru.ifmo.se.johnwick.service.api;

import ru.ifmo.se.johnwick.model.dto.CleaningRequestDto;
import ru.ifmo.se.johnwick.model.entity.OrderEntity;
import ru.ifmo.se.johnwick.model.entity.UserEntity;

import java.util.List;

public interface CleaningRequestService {
    void createCleaningForOrder(OrderEntity order, UserEntity requestedBy);

    List<CleaningRequestDto> listCleanings(String rsqlPredicate);

    CleaningRequestDto updateCleaning(CleaningRequestDto cleaningRequestDto);
}
