package ru.ifmo.se.johnwick.model.dto;

import lombok.Value;
import ru.ifmo.se.johnwick.model.Role;

@Value
public class UserDto {
    String username;
    String displayName;
    Role role;
}
