package ru.ifmo.se.johnwick.model.entity;

import io.quarkus.security.jpa.Password;
import io.quarkus.security.jpa.Roles;
import io.quarkus.security.jpa.UserDefinition;
import io.quarkus.security.jpa.Username;
import jakarta.persistence.*;
import lombok.Data;
import ru.ifmo.se.johnwick.model.UserRole;

import java.util.UUID;

@Data
@Entity
@UserDefinition
@Table(name = "\"user\"")
public class UserEntity {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue
    private UUID id;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "display_name", nullable = false)
    private String displayName;

    @Column(name = "password", nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private UserRole role;

    @Roles
    public String getRoleString() {
        return role.name();
    }

    @Password
    public String getPassword() {
        return password;
    }

    @Username
    public String getUsername() {
        return username;
    }
}
