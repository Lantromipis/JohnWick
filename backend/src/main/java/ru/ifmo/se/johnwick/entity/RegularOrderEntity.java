package ru.ifmo.se.johnwick.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;


@Getter
@Setter
@Entity
@Table(name = "\"regular_order\"")
public class RegularOrderEntity {
    @Id
    @Column(name = "order_id", nullable = false)
    private UUID orderId = UUID.randomUUID();

    @ManyToOne
    @JoinColumn(name = "assigned_killer_id")
    private UserEntity assignee;

    @Column(name = "price")
    private Double price;

    @Column(name = "customer_name")
    private String customerName;
}
