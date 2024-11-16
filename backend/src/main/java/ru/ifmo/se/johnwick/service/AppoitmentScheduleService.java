package ru.ifmo.se.johnwick.service;

import io.quarkus.security.identity.SecurityIdentity;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
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
    EntityManager entityManager;

    @Inject
    AppoitmentScheduleMapper appoitmentScheduleMapper;

    public AppointmentScheduleDto createAppointmentSchedule(AppoitmentScheduleInput appoitmentScheduleInput) {
        AppointmentScheduleEntity appointmentScheduleEntity = appoitmentScheduleMapper.mapInputToEntity(appoitmentScheduleInput);
        String username = securityIdentity.getPrincipal().getName();
        UserEntity hooster = userRepository.findByUsername(username);
        appointmentScheduleEntity.setHoster(hooster);
        entityManager.persist(appointmentScheduleEntity);
        return appoitmentScheduleMapper.mapEntityToDto(appointmentScheduleEntity);
    }

    public Collection<AppointmentScheduleDto> getAppointmentScheduleByDateRange(LocalDate startDate,LocalDate endDate) {
        Collection<AppointmentScheduleEntity> entityCollection = appoitmentScheduleRepository.findByDate(startDate,endDate);
        return appoitmentScheduleMapper.mapEntitiesToDtos(entityCollection);
    }
}
