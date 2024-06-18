package ru.ifmo.se.johnwick.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import ru.ifmo.se.johnwick.entity.OrderEntity;
import ru.ifmo.se.johnwick.model.input.HeadHuntOrderInput;
import ru.ifmo.se.johnwick.model.input.PromissoryNoteOrderInput;
import ru.ifmo.se.johnwick.model.dto.OrderDto;
import ru.ifmo.se.johnwick.model.input.RegularOrderInput;

import java.util.Collection;

@Mapper(componentModel = MappingConstants.ComponentModel.JAKARTA,
        uses = UserMapper.class)
public interface OrderMapper {
    OrderEntity mapInputToEntity(RegularOrderInput input);

    OrderEntity mapInputToEntity(HeadHuntOrderInput input);

    @Mapping(expression = "java( ru.ifmo.se.johnwick.entity.UserEntity.findByUsername(input.getDebtorUsername()) )", target = "debtor")
    OrderEntity mapInputToEntity(PromissoryNoteOrderInput input);

    OrderDto entityToDto(OrderEntity entity);

    Collection<OrderDto> entitiesToDtos(Collection<OrderEntity> entity);
}
