package ru.ifmo.se.johnwick.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.ifmo.se.johnwick.entity.AppointmentEntity;
import ru.ifmo.se.johnwick.entity.AppointmentScheduleEntity;
import ru.ifmo.se.johnwick.entity.UserEntity;
import ru.ifmo.se.johnwick.model.dto.AppointmentScheduleDto;
import ru.ifmo.se.johnwick.model.dto.UserDto;
import ru.ifmo.se.johnwick.model.input.AppoitmentScheduleInput;
import ru.ifmo.se.johnwick.model.input.UserInput;

import java.util.Collection;

@Mapper(componentModel = MappingConstants.ComponentModel.JAKARTA)
public interface AppoitmentScheduleMapper {

    AppointmentScheduleDto mapEntityToDto(AppointmentScheduleEntity appointmentScheduleEntity);

    Collection<AppointmentScheduleDto> mapEntitiesToDtos(Collection<AppointmentScheduleEntity> entityCollection);

    AppointmentScheduleEntity mapInputToEntity(AppoitmentScheduleInput appoitmentScheduleInput);
}

