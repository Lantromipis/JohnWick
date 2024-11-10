package ru.ifmo.se.johnwick.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "\"regular_order_application\"")
public class RegularOrderApplicatonEntity {
    @Id
    @Column(name = "id", nullable = false)
    private UUID id = UUID.randomUUID();

    @ManyToOne
    @JoinColumn(name = "applied_killer_id")
    private UserEntity killer;

    @ManyToOne
    @JoinColumn(name = "regular_order_id")
    private RegularOrderEntity regularOrder;

    @Column(name = "created_timestamp", nullable = false, insertable = false, updatable = false)
    private Instant createdTimestamp;
}
