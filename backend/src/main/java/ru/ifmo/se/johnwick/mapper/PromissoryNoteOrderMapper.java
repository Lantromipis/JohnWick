package ru.ifmo.se.johnwick.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.ifmo.se.johnwick.entity.AppointmentScheduleEntity;
import ru.ifmo.se.johnwick.entity.PromissoryNoteOrderEntity;
import ru.ifmo.se.johnwick.model.dto.AppointmentScheduleDto;
import ru.ifmo.se.johnwick.model.dto.PromissoryNoteOrderDto;
import ru.ifmo.se.johnwick.model.input.AppoitmentScheduleInput;
import ru.ifmo.se.johnwick.model.input.PromissoryNoteOrderInput;

import java.util.Collection;

@Mapper(componentModel = MappingConstants.ComponentModel.JAKARTA)
public interface PromissoryNoteOrderMapper {

    PromissoryNoteOrderDto mapEntityToDto(PromissoryNoteOrderEntity promissoryNoteOrderEntity);

    Collection<PromissoryNoteOrderDto> mapEntitiesToDtos(Collection<PromissoryNoteOrderEntity> entityCollection);

    PromissoryNoteOrderEntity mapInputToEntity(PromissoryNoteOrderInput promissoryNoteOrderInput);
}
