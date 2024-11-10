package ru.ifmo.se.johnwick.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "\"notification\"")
public class NotificationEntity {
    @Id
    @Column(name = "id", nullable = false)
    private UUID id = UUID.randomUUID();

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity addressee;

    @Column(name = "title")
    private String title;

    @Column(name = "message")
    private String message;

    @Column(name = "created_timestamp", nullable = false, insertable = false, updatable = false)
    private Instant createdTimestamp;

    @Column(name = "is_read", nullable = false)
    private Boolean isRead = false;

}
