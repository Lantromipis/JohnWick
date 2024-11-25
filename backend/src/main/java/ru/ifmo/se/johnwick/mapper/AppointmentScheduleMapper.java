package ru.ifmo.se.johnwick.mapper;

import org.mapstruct.*;
import ru.ifmo.se.johnwick.model.dto.AppointmentDto;
import ru.ifmo.se.johnwick.model.dto.AppointmentScheduleDto;
import ru.ifmo.se.johnwick.model.entity.AppointmentEntity;
import ru.ifmo.se.johnwick.model.entity.AppointmentScheduleEntity;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.JAKARTA_CDI, uses = {UserMapper.class})
public abstract class AppointmentScheduleMapper {

    @Named("appointmentsWithoutSchedule")
    @Mapping(target = "appointments", qualifiedByName = "noSchedule")
    public abstract AppointmentScheduleDto mapAppointmentScheduleEntityToDto(AppointmentScheduleEntity entity);

    @IterableMapping(qualifiedByName = "appointmentsWithoutSchedule")
    public abstract List<AppointmentScheduleDto> mapAppointmentScheduleEntityToDto(List<AppointmentScheduleEntity> entities);

    @Named("appointmentsWithoutBookerAndSchedule")
    @Mapping(target = "appointments", qualifiedByName = "noBookerNoSchedule")
    public abstract AppointmentScheduleDto mapAppointmentScheduleEntityToDtoWithoutBookerAndSchedule(AppointmentScheduleEntity entity);

    @IterableMapping(qualifiedByName = "appointmentsWithoutBookerAndSchedule")
    public abstract List<AppointmentScheduleDto> mapAppointmentScheduleEntityToDtoWithoutBookerAndSchedule(List<AppointmentScheduleEntity> entity);

    public abstract AppointmentScheduleEntity mapAppointmentScheduleDtoToEntity(AppointmentScheduleDto dto);

    @Named("noBookerNoSchedule")
    @Mapping(target = "bookedBy", ignore = true)
    @Mapping(target = "appointmentSchedule", ignore = true)
    public abstract AppointmentDto mapAppointmentEntityToDtoWithoutBookerAndSchedule(AppointmentEntity entity);

    @IterableMapping(qualifiedByName = "noBookerNoSchedule")
    public abstract List<AppointmentDto> mapAppointmentEntityToDtoWithoutBookerAndSchedule(List<AppointmentEntity> entities);

    @Named("noSchedule")
    @Mapping(target = "appointmentSchedule", ignore = true)
    public abstract AppointmentDto mapAppointmentEntityToDtoWithoutSchedule(AppointmentEntity entity);

    @Named("noAppointmentsOnSchedule")
    @Mapping(target = "appointmentSchedule", qualifiedByName = "appointmentsWithoutSchedule")
    public abstract AppointmentDto mapAppointmentEntityToDto(AppointmentEntity entity);

    @IterableMapping(qualifiedByName = "noAppointmentsOnSchedule")
    public abstract List<AppointmentDto> mapAppointmentEntityToDto(List<AppointmentEntity> entity);

    public abstract AppointmentEntity mapAppointmentDtoToEntity(AppointmentDto dto);
}
