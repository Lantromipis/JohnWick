package ru.ifmo.se.johnwick.model.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "\"notification\"")
public class NotificationEntity {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity recipient;

    @Column(name = "title")
    private String title;

    @Column(name = "message")
    private String message;

    @Column(name = "created_timestamp", nullable = false, insertable = false, updatable = false)
    private OffsetDateTime createdTimestamp;

    @Column(name = "is_read", nullable = false)
    private boolean read;
}
