package ru.ifmo.se.johnwick.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@Table(name = "\"regular_order\"")
@EqualsAndHashCode(callSuper = true)
@PrimaryKeyJoinColumn(name = "order_id")
public class RegularOrderEntity extends OrderEntity {
    @ManyToOne
    @JoinColumn(name = "assigned_killer_id")
    private UserEntity assignee;

    @Column(name = "price")
    private Double price;

    @Column(name = "customer_name")
    private String customerName;
}
