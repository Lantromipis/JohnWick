package ru.ifmo.se.johnwick.mapper;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Mappings;
import ru.ifmo.se.johnwick.entity.AppointmentScheduleEntity;
import ru.ifmo.se.johnwick.entity.OrderEntity;

import ru.ifmo.se.johnwick.model.dto.AppointmentScheduleDto;
import ru.ifmo.se.johnwick.model.dto.OrderDto;

import ru.ifmo.se.johnwick.model.input.OrderInput;

import java.util.Collection;

@Mapper(componentModel = MappingConstants.ComponentModel.JAKARTA)
public interface OrderMapper {

    OrderDto mapEntityToDto(OrderEntity order);

    @Mappings({
            @Mapping(expression = "java( ru.ifmo.se.johnwick.model.OrderStatus.valueOf(orderInput.getStatus()) )", target = "status"),
            @Mapping(expression = "java( ru.ifmo.se.johnwick.model.OrderType.valueOf(orderInput.getType()) )", target = "type"),
    })
    OrderEntity mapInputToEntity(OrderInput orderInput);

    Collection<OrderDto> mapEntitiesToDtos(Collection<OrderEntity> entityCollection);
}
