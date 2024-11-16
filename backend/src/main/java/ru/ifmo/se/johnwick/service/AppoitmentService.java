package ru.ifmo.se.johnwick.service;

import io.quarkus.security.identity.SecurityIdentity;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import ru.ifmo.se.johnwick.entity.AppointmentEntity;
import ru.ifmo.se.johnwick.entity.UserEntity;
import ru.ifmo.se.johnwick.mapper.AppoitmentMapper;
import ru.ifmo.se.johnwick.model.dto.AppoitmentDto;
import ru.ifmo.se.johnwick.model.input.AppoitmentInput;
import ru.ifmo.se.johnwick.repository.UserRepository;


@ApplicationScoped
public class AppoitmentService {

    @Inject
    SecurityIdentity securityIdentity;

    @Inject
    UserRepository userRepository;

    @Inject
    AppoitmentMapper appoitmentMapper;

    @Inject
    EntityManager entityManager;

    public AppoitmentDto createAppointmentSchedule(AppoitmentInput appoitmentInput) {
        AppointmentEntity appointmentEntity = appoitmentMapper.mapInputToEntity(appoitmentInput);
        String username = securityIdentity.getPrincipal().getName();
        UserEntity booker = userRepository.findByUsername(username);
        appointmentEntity.setBooker(booker);
        entityManager.persist(appointmentEntity);
        return appoitmentMapper.mapEntityToDto(appointmentEntity);
    }
}
