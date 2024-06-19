package ru.ifmo.se.johnwick.mapper;

import jakarta.inject.Inject;
import org.mapstruct.Context;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Mappings;
import ru.ifmo.se.johnwick.entity.OrderEntity;
import ru.ifmo.se.johnwick.entity.UserEntity;
import ru.ifmo.se.johnwick.model.dto.AvailableOrderDto;
import ru.ifmo.se.johnwick.model.input.HeadHuntOrderInput;
import ru.ifmo.se.johnwick.model.input.PromissoryNoteOrderInput;
import ru.ifmo.se.johnwick.model.dto.OrderDto;
import ru.ifmo.se.johnwick.model.input.RegularOrderInput;
import ru.ifmo.se.johnwick.service.OrderService;

import java.util.Collection;

@Mapper(componentModel = MappingConstants.ComponentModel.JAKARTA,
        uses = UserMapper.class)
public abstract class OrderMapper {
    @Inject
    OrderService orderService;

    public abstract OrderEntity mapInputToEntity(RegularOrderInput input);

    public abstract OrderEntity mapInputToEntity(HeadHuntOrderInput input);

    @Mappings({
            @Mapping(expression = "java( ru.ifmo.se.johnwick.entity.UserEntity.findByUsername(input.getBeneficiary().getUsername()) )", target = "beneficiary"),
            @Mapping(expression = "java( ru.ifmo.se.johnwick.entity.UserEntity.findByUsername(input.getAssignee().getUsername()) )", target = "assignee")
    })
    public abstract OrderEntity mapInputToEntity(PromissoryNoteOrderInput input);

    public abstract OrderDto entityToDto(OrderEntity entity);

    public abstract Collection<OrderDto> entitiesToDtos(Collection<OrderEntity> entity);

    @Mapping(expression = "java( orderService.hasKillerAppliedToOrder(orderEntity, killer) )", target = "alreadyApplied")
    public abstract AvailableOrderDto entityToAvailableDto(OrderEntity orderEntity, @Context UserEntity killer);

    @InheritConfiguration(name = "entityToAvailableDto")
    public abstract Collection<AvailableOrderDto> entitiesToAvailableDtos(Collection<OrderEntity> entities,
                                                                          @Context UserEntity killer);
}
