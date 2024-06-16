package ru.ifmo.se.johnwick.entity;

import io.quarkus.panache.common.Page;
import io.quarkus.security.jpa.Password;
import io.quarkus.security.jpa.Roles;
import io.quarkus.security.jpa.UserDefinition;
import io.quarkus.security.jpa.Username;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import ru.ifmo.se.johnwick.model.Role;

import java.util.Collection;

@Entity
@UserDefinition
@Table(name = "application_user")
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

    public Role getRole() {
        return role;
    }

    @Roles
    public String getRoleString() {
        return role.getString();
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @Password
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    @Username
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public static UserEntity findByUsername(String username) {
        return find("username", username).firstResult();
    }

    public static Collection<UserEntity> findByPage(Page page) {
        return findAll().page(page).list();
    }
}