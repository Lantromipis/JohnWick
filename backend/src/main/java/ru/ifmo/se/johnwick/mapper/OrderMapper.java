package ru.ifmo.se.johnwick.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import ru.ifmo.se.johnwick.entity.OrderEntity;
import ru.ifmo.se.johnwick.model.BillOrderInput;
import ru.ifmo.se.johnwick.model.Order;
import ru.ifmo.se.johnwick.model.TargetOrderInput;

import java.util.Collection;

@Mapper(componentModel = MappingConstants.ComponentModel.JAKARTA, uses = UserMapper.class)
public interface OrderMapper {
    OrderEntity mapInputToEntity(TargetOrderInput input);

    @Mapping(expression = "java( ru.ifmo.se.johnwick.entity.UserEntity.findByUsername(input.getDebtorUsername()) )", target = "debtor")
    OrderEntity mapInputToEntity(BillOrderInput input);

    Order entityToDto(OrderEntity entity);

    Collection<Order> entitiesToDtos(Collection<OrderEntity> entity);
}
