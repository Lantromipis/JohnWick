package ru.ifmo.se.johnwick.service;

import io.quarkus.security.identity.SecurityIdentity;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;
import ru.ifmo.se.johnwick.entity.AppointmentScheduleEntity;
import ru.ifmo.se.johnwick.entity.UserEntity;
import ru.ifmo.se.johnwick.mapper.AppoitmentScheduleMapper;
import ru.ifmo.se.johnwick.model.dto.AppointmentScheduleDto;
import ru.ifmo.se.johnwick.model.input.AppoitmentScheduleInput;
import ru.ifmo.se.johnwick.repository.AppoitmentScheduleRepository;
import ru.ifmo.se.johnwick.repository.UserRepository;

import java.time.LocalDate;
import java.util.Collection;

@ApplicationScoped
public class AppoitmentScheduleService {

    @Inject
    AppoitmentScheduleRepository appoitmentScheduleRepository;

    @Inject
    SecurityIdentity securityIdentity;

    @Inject
    UserRepository userRepository;

    @Inject
    AppoitmentScheduleMapper appoitmentScheduleMapper;

    public AppointmentScheduleDto createAppointmentSchedule(AppoitmentScheduleInput appoitmentScheduleInput){
        AppointmentScheduleEntity appointmentScheduleEntity = appoitmentScheduleMapper.mapInputToEntity(appoitmentScheduleInput);
        appointmentScheduleEntity.persist();
        String username = securityIdentity.getPrincipal().getName();
        UserEntity hooster =  userRepository.findByUsername(username);
        appointmentScheduleEntity.setHoster(hooster);
        appointmentScheduleEntity.persist();
        return appoitmentScheduleMapper.mapEntityToDto(appointmentScheduleEntity);
    }

    public Collection <AppointmentScheduleDto> getAppointmentScheduleByDate(LocalDate date) {
        Collection<AppointmentScheduleEntity> entityCollection = appoitmentScheduleRepository.findByDate(date);
        return appoitmentScheduleMapper.mapEntitiesToDtos(entityCollection);
    }
}
