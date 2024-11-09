package ru.ifmo.se.johnwick.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.security.jpa.Password;
import io.quarkus.security.jpa.Roles;
import io.quarkus.security.jpa.UserDefinition;
import io.quarkus.security.jpa.Username;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import ru.ifmo.se.johnwick.model.Role;

import java.util.UUID;

@Getter
@Setter
@Entity
@UserDefinition
@Table(name = "\"user\"")
public class UserEntity extends PanacheEntityBase {
    @Id
    @Column(name = "id", nullable = false)
    private UUID id = UUID.randomUUID();

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "display_name", nullable = false, unique = true)
    private String displayName;

    @Column(name = "password", nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role;

    @Column(name = "status", nullable = false)
    private String status;

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

    public UUID getId() {
        return id;
    }
}