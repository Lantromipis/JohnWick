package ru.ifmo.se.johnwick.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import ru.ifmo.se.johnwick.model.OrderType;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "\"order\"")
public class OrderEntity extends PanacheEntityBase {
    @Id
    @Column(name = "id", nullable = false)
    private UUID id = UUID.randomUUID();

    @Column(name = "created_timestamp")
    private Instant createdTimestamp;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private OrderType type;

    @Column(name = "description")
    private String description;

    @Column(name = "status")
    private String status;

    @Column(name = "target_name")
    private String targetName;

}
