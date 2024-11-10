package ru.ifmo.se.johnwick.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import ru.ifmo.se.johnwick.model.OrderStatus;
import ru.ifmo.se.johnwick.model.OrderType;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "\"order\"")
public class OrderEntity  {
    @Id
    @Column(name = "id", nullable = false)
    private UUID id = UUID.randomUUID();

    @Column(name = "created_timestamp", nullable = false, updatable = false)
    private Instant createdTimestamp;

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
