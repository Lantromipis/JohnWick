package ru.ifmo.se.johnwick.service.impl;

import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import ru.ifmo.se.johnwick.exception.EntityNotFoundByIdException;
import ru.ifmo.se.johnwick.exception.ValidationException;
import ru.ifmo.se.johnwick.mapper.UserMapper;
import ru.ifmo.se.johnwick.model.dto.UserDto;
import ru.ifmo.se.johnwick.model.entity.UserEntity;
import ru.ifmo.se.johnwick.repository.UserRepository;
import ru.ifmo.se.johnwick.service.api.UserService;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class UserServiceImpl implements UserService {

    @Inject
    UserMapper userMapper;

    @Inject
    UserRepository userRepository;

    @Override
    public List<UserDto> listUsers() {
        List<UserEntity> entities = userRepository.listAll(Sort.by("username"));
        return userMapper.toDto(entities);
    }

    @Override
    public UserDto getUserById(UUID id) {
        UserEntity entity = userRepository.findById(id);
        if (entity == null) {
            throw new EntityNotFoundByIdException("User", id.toString());
        }
        return userMapper.toDto(entity);
    }

    @Override
    public UserDto getUserByUsername(String username) {
        UserEntity entity = userRepository.findByUsername(username);
        if (entity == null) {
            throw new EntityNotFoundByIdException("User", username);
        }
        return userMapper.toDto(entity);
    }

    @Override
    @Transactional
    public UserDto createUser(UserDto user) {
        UserEntity userEntity = userMapper.fromDtoWithPassword(user);
        UserEntity existingUser = userRepository.findByUsername(userEntity.getUsername());
        if (existingUser != null) {
            throw new ValidationException("User with provided username already exists");
        }
        userEntity.setId(null);
        userRepository.persistAndFlush(userEntity);
        return userMapper.toDto(userEntity);
    }

    @Override
    @Transactional
    public UserDto updateUser(UserDto user) {
        UserEntity existingUser = userRepository.findById(user.getId());
        if (existingUser == null) {
            throw new EntityNotFoundByIdException("User", user.getId().toString());
        }

        UserEntity newUserEntity = userMapper.fromDto(user);
        if (newUserEntity.getPassword() != null) {
            existingUser.setPassword(newUserEntity.getPassword());
        }
        if (newUserEntity.getDisplayName() != null) {
            existingUser.setDisplayName(newUserEntity.getDisplayName());
        }
        if (newUserEntity.getUsername() != null) {
            existingUser.setUsername(newUserEntity.getUsername());
        }

        userRepository.persistAndFlush(existingUser);
        return userMapper.toDto(existingUser);
    }
}
