package ru.ifmo.se.johnwick.mapper;


import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.ifmo.se.johnwick.entity.OrderEntity;
import ru.ifmo.se.johnwick.entity.RegularOrderEntity;
import ru.ifmo.se.johnwick.model.dto.OrderDto;
import ru.ifmo.se.johnwick.model.dto.RegularOrderDto;
import ru.ifmo.se.johnwick.model.input.OrderInput;
import ru.ifmo.se.johnwick.model.input.RegularOrderInput;

@Mapper(componentModel = MappingConstants.ComponentModel.JAKARTA)
public interface RegularOrderMapper {

    RegularOrderDto mapEntityToDto(RegularOrderEntity regularOrderEntity);

    RegularOrderEntity mapInputToEntity(RegularOrderInput regularOrderInput);
}
