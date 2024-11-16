package ru.ifmo.se.johnwick.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import ru.ifmo.se.johnwick.model.CleaningRequestStatus;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "\"cleaning_request\"")
public class CleaningRequestEntity {
    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private OrderEntity order;

    @ManyToOne
    @JoinColumn(name = "requested_by", nullable = false)
    private UserEntity requestedBy;

    @Column(name = "created_timestamp", nullable = false, updatable = false)
    private OffsetDateTime createdTimestamp;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private CleaningRequestStatus status;

    @Column(name = "details")
    private String details;
}
