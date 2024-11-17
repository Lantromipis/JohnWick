package ru.ifmo.se.johnwick.service.api;

import ru.ifmo.se.johnwick.model.dto.RegularOrderApplicationDto;
import ru.ifmo.se.johnwick.model.dto.RegularOrderDto;

import java.util.List;
import java.util.UUID;

public interface RegularOrderService {
    RegularOrderDto createRegularOrder(RegularOrderDto regularOrderDto);

    RegularOrderDto getRegularOrder(UUID orderId);

    List<RegularOrderDto> listRegularOrders(String rsqlPredicate);

    List<RegularOrderApplicationDto> listRegularOrderApplications(String rsqlPredicate);

    RegularOrderApplicationDto createRegularOrderApplication(UUID orderId);

    RegularOrderDto updateRegularOrder(RegularOrderDto regularOrderDto);
}
