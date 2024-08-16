package ru.ifmo.se.johnwick.entity;

import io.quarkus.security.jpa.Password;
import io.quarkus.security.jpa.Roles;
import io.quarkus.security.jpa.UserDefinition;
import io.quarkus.security.jpa.Username;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import ru.ifmo.se.johnwick.model.Role;

@Getter
@Setter
@Entity
@UserDefinition
@Table(name = "\"user\"")
public class UserEntity extends BasicEntity {
    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "display_name", nullable = false, unique = true)
    private String displayName;

    @Column(name = "password", nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role;

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