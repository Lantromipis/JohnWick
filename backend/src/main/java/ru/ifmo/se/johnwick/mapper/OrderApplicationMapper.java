package ru.ifmo.se.johnwick.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.ifmo.se.johnwick.entity.OrderApplicationEntity;
import ru.ifmo.se.johnwick.model.OrderApplicationDto;

import java.util.Collection;

@Mapper(componentModel = MappingConstants.ComponentModel.JAKARTA,
        uses = {OrderMapper.class, UserMapper.class})
public interface OrderApplicationMapper {
    OrderApplicationDto entityToDto(OrderApplicationEntity entity);

    Collection<OrderApplicationDto> entitiesToDtos(Collection<OrderApplicationEntity> entities);
}
