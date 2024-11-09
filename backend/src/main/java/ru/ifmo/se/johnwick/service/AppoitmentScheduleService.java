package ru.ifmo.se.johnwick.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;
import ru.ifmo.se.johnwick.entity.AppointmentScheduleEntity;
import ru.ifmo.se.johnwick.entity.UserEntity;
import ru.ifmo.se.johnwick.mapper.AppoitmentScheduleMapper;
import ru.ifmo.se.johnwick.model.dto.AppointmentScheduleDto;
import ru.ifmo.se.johnwick.model.input.AppoitmentScheduleInput;
import ru.ifmo.se.johnwick.repository.AppoitmentScheduleRepository;

import java.time.LocalDate;
import java.util.Collection;

@ApplicationScoped
public class AppoitmentScheduleService {


    @Inject
    AppoitmentScheduleRepository appoitmentScheduleRepository;

    @Inject
    AppoitmentScheduleMapper appoitmentScheduleMapper;

    /*public AppointmentScheduleDto createAppointmentSchedule(AppoitmentScheduleInput appoitmentScheduleInput){
        AppointmentScheduleEntity appointmentScheduleEntity = appoitmentScheduleMapper.mapInputToEntity(appoitmentScheduleInput);
        appointmentScheduleEntity.persist();
        LOG.info(appointmentScheduleEntity);
        return appoitmentScheduleMapper.mapEntityToDto(appointmentScheduleEntity);
    }*/

    public Collection <AppointmentScheduleDto> getAppointmentScheduleByDate(LocalDate date) {
        Collection<AppointmentScheduleEntity> entityCollection = appoitmentScheduleRepository.findByDate(date);
        return appoitmentScheduleMapper.mapEntitiesToDtos(entityCollection);
    }
}
