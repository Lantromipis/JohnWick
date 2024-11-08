package ru.ifmo.se.johnwick.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "\"head_haunt_order\"")
public class HeadHuntOrderEntity extends PanacheEntityBase {
    @Id
    @Column(name = "order_id", nullable = false)
    private UUID orderId = UUID.randomUUID();

    @ManyToOne
    @JoinColumn(name = "succeded_killer_id", nullable = false)
    private UserEntity successer;

    @Column(name = "current_price", nullable = false)
    private Double currentPrice;

    @Column(name = "customer_name")
    private String customerName;
}
