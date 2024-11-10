package ru.ifmo.se.johnwick.model.input;

import lombok.Data;

@Data
public class UserInput {
    String username;
    String displayName;
    String role;
    String password;
}
