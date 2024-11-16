package ru.ifmo.se.johnwick.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import ru.ifmo.se.johnwick.model.OrderStatus;
import ru.ifmo.se.johnwick.model.OrderType;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "\"order\"")
@Inheritance(strategy = InheritanceType.JOINED)
public class OrderEntity {
    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "created_timestamp", nullable = false, updatable = false)
    private OffsetDateTime createdTimestamp;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private OrderType type;

    @Column(name = "description")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private OrderStatus status;

    @Column(name = "target_name")
    private String targetName;
}
