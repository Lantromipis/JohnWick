package ru.ifmo.se.johnwick.mapper;


import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.ifmo.se.johnwick.entity.OrderEntity;

import ru.ifmo.se.johnwick.model.dto.OrderDto;

import ru.ifmo.se.johnwick.model.input.OrderInput;

@Mapper(componentModel = MappingConstants.ComponentModel.JAKARTA)
public interface OrderMapper {

    OrderDto mapEntityToDto(OrderEntity order);

    OrderEntity mapInputToEntity(OrderInput orderInput);
}
