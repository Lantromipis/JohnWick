package ru.ifmo.se.johnwick.model.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "\"regular_order_application\"")
public class RegularOrderApplicationEntity {
    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "applied_killer_id")
    private UserEntity killer;

    @ManyToOne
    @JoinColumn(name = "regular_order_id")
    private RegularOrderEntity regularOrder;

    @Column(name = "created_timestamp", nullable = false, insertable = false, updatable = false)
    private OffsetDateTime createdTimestamp;
}
