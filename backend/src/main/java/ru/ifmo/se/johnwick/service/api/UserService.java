package ru.ifmo.se.johnwick.service.api;

import ru.ifmo.se.johnwick.model.dto.UserDto;

import java.util.List;
import java.util.UUID;

public interface UserService {
    List<UserDto> listUsers();

    UserDto getUserById(UUID id);

    UserDto getUserByUsername(String username);

    UserDto createUser(UserDto user);

    UserDto updateUser(UserDto user);
}
