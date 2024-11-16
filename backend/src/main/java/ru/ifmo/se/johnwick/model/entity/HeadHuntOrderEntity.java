package ru.ifmo.se.johnwick.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@Table(name = "\"head_haunt_order\"")
@EqualsAndHashCode(callSuper = true)
@PrimaryKeyJoinColumn(name = "order_id")
public class HeadHuntOrderEntity extends OrderEntity {
    @ManyToOne
    @JoinColumn(name = "succeeded_killer_id")
    private UserEntity succeededKiller;

    @Column(name = "current_price", nullable = false)
    private Double currentPrice;

    @Column(name = "customer_name")
    private String customerName;
}
