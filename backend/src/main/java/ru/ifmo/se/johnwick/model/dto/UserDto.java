package ru.ifmo.se.johnwick.model.dto;

import lombok.Value;
import ru.ifmo.se.johnwick.model.Role;

import java.util.UUID;

@Value
public class UserDto {
    UUID id;
    String username;
    String displayName;
    Role role;
}
