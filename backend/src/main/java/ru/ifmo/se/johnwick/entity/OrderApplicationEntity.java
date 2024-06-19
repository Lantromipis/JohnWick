package ru.ifmo.se.johnwick.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Collection;

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

    public static Collection<OrderApplicationEntity> findByOrder(OrderEntity order) {
        return find("order = ?1", order).list();
    }

    public static long countByOrderAndAppliedKiller(OrderEntity order, UserEntity appliedKiller) {
        return find("order = ?1 and appliedKiller = ?2", order, appliedKiller).count();
    }
}