package ru.ifmo.se.johnwick.model;

import lombok.Value;

@Value
public class UserDto {
    String username;
    String displayName;
    Role role;
}
