package ru.ifmo.se.johnwick.service;

import io.quarkus.security.identity.SecurityIdentity;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import ru.ifmo.se.johnwick.entity.OrderEntity;
import ru.ifmo.se.johnwick.entity.RegularOrderEntity;
import ru.ifmo.se.johnwick.entity.UserEntity;
import ru.ifmo.se.johnwick.mapper.RegularOrderMapper;
import ru.ifmo.se.johnwick.model.dto.OrderDto;
import ru.ifmo.se.johnwick.model.dto.RegularOrderDto;
import ru.ifmo.se.johnwick.model.input.OrderInput;
import ru.ifmo.se.johnwick.model.input.RegularOrderInput;
import ru.ifmo.se.johnwick.repository.UserRepository;

import java.time.Instant;

@ApplicationScoped
public class RegularOrderService {

    @Inject
    RegularOrderMapper regularOrderMapper;

    @Inject
    SecurityIdentity securityIdentity;

    @Inject
    UserRepository userRepository;

    public RegularOrderDto createRegularOrder(RegularOrderInput regularOrderInput) {
        RegularOrderEntity entity = regularOrderMapper.mapInputToEntity(regularOrderInput);
        entity.persist();
        return regularOrderMapper.mapEntityToDto(entity);
    }
}
