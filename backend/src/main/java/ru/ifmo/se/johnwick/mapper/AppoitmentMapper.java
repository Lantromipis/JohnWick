package ru.ifmo.se.johnwick.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.ifmo.se.johnwick.entity.AppointmentEntity;
import ru.ifmo.se.johnwick.entity.AppointmentScheduleEntity;
import ru.ifmo.se.johnwick.model.dto.AppointmentScheduleDto;
import ru.ifmo.se.johnwick.model.dto.AppoitmentDto;
import ru.ifmo.se.johnwick.model.input.AppoitmentInput;
import ru.ifmo.se.johnwick.model.input.AppoitmentScheduleInput;

import java.util.Collection;

@Mapper(componentModel = MappingConstants.ComponentModel.JAKARTA)
public interface AppoitmentMapper {

    AppoitmentDto mapEntityToDto(AppointmentEntity appointmentEntity);

    Collection<AppoitmentDto> mapEntitiesToDtos(Collection<AppointmentEntity> entityCollection);

    AppointmentEntity mapInputToEntity(AppoitmentInput appoitmentInput);
}
