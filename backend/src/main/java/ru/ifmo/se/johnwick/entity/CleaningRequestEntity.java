package ru.ifmo.se.johnwick.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import ru.ifmo.se.johnwick.model.CleaningRequestStatus;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "\"cleaning_request\"")
public class CleaningRequestEntity {
    @Id
    @Column(name = "id", nullable = false)
    private UUID id = UUID.randomUUID();

    @ManyToOne
    @JoinColumn(name = "order_id")
    private OrderEntity order;

    @ManyToOne
    @JoinColumn(name = "requested_by", nullable = false)
    private UserEntity requester;

    @Column(name = "created_timestamp", nullable = false)
    private Instant createdTimestamp;

    @Column(name = "price", nullable = false)
    private Double price;

    @Column(name = "status", nullable = false)
    private CleaningRequestStatus status;

    @Column(name = "details")
    private String details;
}
