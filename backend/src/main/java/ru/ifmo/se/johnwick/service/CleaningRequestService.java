package ru.ifmo.se.johnwick.service;

import io.quarkus.security.identity.SecurityIdentity;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import ru.ifmo.se.johnwick.entity.CleaningRequestEntity;
import ru.ifmo.se.johnwick.entity.OrderEntity;
import ru.ifmo.se.johnwick.entity.UserEntity;
import ru.ifmo.se.johnwick.mapper.CleaningRequestMapper;
import ru.ifmo.se.johnwick.mapper.OrderMapper;
import ru.ifmo.se.johnwick.model.CleaningRequestStatus;
import ru.ifmo.se.johnwick.model.dto.CleaningRequestDto;
import ru.ifmo.se.johnwick.model.dto.OrderDto;
import ru.ifmo.se.johnwick.model.input.CleaningRequestInput;
import ru.ifmo.se.johnwick.model.input.OrderInput;
import ru.ifmo.se.johnwick.repository.CleaningRequestRepository;
import ru.ifmo.se.johnwick.repository.OrderRepository;
import ru.ifmo.se.johnwick.repository.UserRepository;

import java.time.Instant;
import java.util.Collection;

@ApplicationScoped
public class CleaningRequestService {

    @Inject
    SecurityIdentity securityIdentity;

    @Inject
    EntityManager entityManager;

    @Inject
    CleaningRequestMapper cleaningRequestMapper;

    @Inject
    UserRepository userRepository;

    @Inject
    CleaningRequestRepository cleaningRequestRepository;

    public CleaningRequestDto createCleaningRequest(CleaningRequestInput cleaningRequestInput) {
        CleaningRequestEntity cleaningRequestEntity = cleaningRequestMapper.mapInputToEntity(cleaningRequestInput);
        String username = securityIdentity.getPrincipal().getName();
        UserEntity requster = userRepository.findByUsername(username);
        cleaningRequestEntity.setRequester(requster);
        cleaningRequestEntity.setCreatedTimestamp(Instant.now());
        entityManager.persist(cleaningRequestEntity);
        return cleaningRequestMapper.mapEntityToDto(cleaningRequestEntity);
    }
    public Collection<CleaningRequestDto> getAllCleaningRequest() {
        Collection<CleaningRequestEntity> entityCollection = cleaningRequestRepository.findAll().list();
        return cleaningRequestMapper.mapEntitiesToDtos(entityCollection);
    }
}
