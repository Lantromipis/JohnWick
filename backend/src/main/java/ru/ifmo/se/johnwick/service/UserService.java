package ru.ifmo.se.johnwick.service;

import io.quarkus.elytron.security.common.BcryptUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import ru.ifmo.se.johnwick.entity.UserEntity;
import ru.ifmo.se.johnwick.mapper.UserMapper;
import ru.ifmo.se.johnwick.model.Role;
import ru.ifmo.se.johnwick.model.dto.UserDto;
import ru.ifmo.se.johnwick.model.input.PasswordInput;
import ru.ifmo.se.johnwick.model.input.UserInput;
import ru.ifmo.se.johnwick.repository.UserRepository;

import java.util.Arrays;
import java.util.Collection;
import java.util.UUID;

@ApplicationScoped
public class UserService {

    @Inject
    UserRepository userRepository;

    @Inject
    EntityManager entityManager;

    @Inject
    UserMapper userMapper;

    public UserDto getUser(String userName) {
        return userMapper.mapEntityToDto(getUserEntity(userName));
    }

    public Collection<UserDto> getAllUsers() {
        Collection<UserEntity> entityCollection = userRepository.findAll().list();
        return userMapper.mapEntitiesToDtos(entityCollection);
    }

    public Collection<UserDto> getAllByRoles() {
        Collection<Role> rolesToFind = Arrays.asList(Role.TAILOR, Role.SOMMELIER);
        Collection<UserEntity> entityCollection = userRepository.findByRole(rolesToFind);
        return userMapper.mapEntitiesToDtos(entityCollection);
    }

    public UserDto createUser(UserInput userInput) {
        UserEntity entity = userMapper.mapInputToEntity(userInput);
        entityManager.persist(entity);
        return userMapper.mapEntityToDto(entity);
    }

    public UserDto changeUserPassword(String username, PasswordInput passwordInput) {
        UserEntity entity = userRepository.findByUsername(username);
        entity.setPassword(BcryptUtil.bcryptHash(passwordInput.getPassword()));
        entityManager.persist(entity);
        return userMapper.mapEntityToDto(entity);
    }

    UserEntity getUserEntity(String username) {
        return userRepository.findByUsername(username);
    }
    public UserDto getUserById(UUID id) {
        UserEntity user = userRepository.findByID(id);
        return userMapper.mapEntityToDto(user);
    }
}
