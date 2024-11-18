package ru.ifmo.se.johnwick.mapper;

import org.mapstruct.*;
import ru.ifmo.se.johnwick.model.dto.AppointmentDto;
import ru.ifmo.se.johnwick.model.dto.AppointmentScheduleDto;
import ru.ifmo.se.johnwick.model.entity.AppointmentEntity;
import ru.ifmo.se.johnwick.model.entity.AppointmentScheduleEntity;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.JAKARTA_CDI, uses = {UserMapper.class})
public abstract class AppointmentScheduleMapper {
    public abstract AppointmentScheduleDto appointmentScheduleEntityToDto(AppointmentScheduleEntity entity);

    public abstract List<AppointmentScheduleDto> appointmentScheduleEntityToDto(List<AppointmentScheduleEntity> entities);

    @Named("noBookerSchedule")
    @Mapping(target = "appointments", qualifiedByName = "noBooker")
    public abstract AppointmentScheduleDto appointmentScheduleEntityToDtoWithoutBooker(AppointmentScheduleEntity entity);

    @IterableMapping(qualifiedByName = "noBookerSchedule")
    public abstract List<AppointmentScheduleDto> appointmentScheduleEntityToDtoWithoutBooker(List<AppointmentScheduleEntity> entity);

    public abstract AppointmentScheduleEntity appointmentScheduleDtoToEntity(AppointmentScheduleDto dto);

    public abstract List<AppointmentScheduleEntity> appointmentScheduleDtoToEntity(List<AppointmentScheduleDto> dtos);

    public abstract AppointmentDto appointmentEntityToDto(AppointmentEntity entity);

    @Named("noBooker")
    @Mapping(target = "bookedBy", ignore = true)
    public abstract AppointmentDto appointmentEntityToDtoWithoutBooker(AppointmentEntity entity);

    public abstract List<AppointmentDto> appointmentEntityToDtoWithoutBooker(List<AppointmentEntity> entities);

    public abstract List<AppointmentDto> appointmentEntityToDto(List<AppointmentEntity> entities);

    public abstract AppointmentEntity appointmentEntityToDto(AppointmentDto dto);

    public abstract List<AppointmentEntity> appointmentDtoToEntity(List<AppointmentDto> dtos);
}
