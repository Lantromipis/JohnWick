package ru.ifmo.se.johnwick.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.ifmo.se.johnwick.entity.AppointmentScheduleEntity;
import ru.ifmo.se.johnwick.entity.HeadHuntOrderEntity;
import ru.ifmo.se.johnwick.model.dto.AppointmentScheduleDto;
import ru.ifmo.se.johnwick.model.dto.HeadHuntOrderDto;
import ru.ifmo.se.johnwick.model.input.AppoitmentScheduleInput;
import ru.ifmo.se.johnwick.model.input.HeadHuntOrderInput;

import java.util.Collection;

@Mapper(componentModel = MappingConstants.ComponentModel.JAKARTA)
public interface HeadHuntOrderMapper {

    HeadHuntOrderDto mapEntityToDto(HeadHuntOrderEntity headHuntOrderEntity);

    Collection<HeadHuntOrderDto> mapEntitiesToDtos(Collection<HeadHuntOrderEntity> entityCollection);

    HeadHuntOrderEntity mapInputToEntity(HeadHuntOrderInput headHuntOrderInput);
}
