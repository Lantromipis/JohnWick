package ru.ifmo.se.johnwick.model;

import lombok.Data;

@Data
public class UserInput {
    String username;
    String displayName;
    String role;
    String password;
}
