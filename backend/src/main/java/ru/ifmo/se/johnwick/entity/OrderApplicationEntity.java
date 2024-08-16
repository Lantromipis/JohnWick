package ru.ifmo.se.johnwick.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "order_application")
public class OrderApplicationEntity extends BasicEntity {
    @ManyToOne(optional = false)
    @JoinColumn(name = "applied_killer_id", nullable = false)
    private UserEntity appliedKiller;

    @ManyToOne(optional = false)
    @JoinColumn(name = "order_id", nullable = false)
    private OrderEntity order;
}