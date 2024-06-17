package ru.ifmo.se.johnwick.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import ru.ifmo.se.johnwick.model.OrderType;

import java.util.Collection;

@Getter
@Setter
@Entity
@Table(name = "application_order")
public class OrderEntity extends BasicEntity {
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private OrderType type;

    @ManyToOne
    @JoinColumn(name = "assignee_id")
    private UserEntity assignee;

    @ManyToOne
    @JoinColumn(name = "debtor_id")
    private UserEntity debtor;

    @Column(name = "customer", nullable = false)
    private String customer;

    @Column(name = "target")
    private String target;

    @Column(name = "price", nullable = false)
    private Long price;

    @Column(name = "description")
    private String description;

    public static Collection<OrderEntity> findBillsByDebtor(UserEntity entity) {
        return find("type = ?1 and debtor = ?2", OrderType.BILL, entity).list();
    }

    public static Collection<OrderEntity> findAvailableOrders() {
        return find("type = ?1 or ( type = ?2 and assignee is null )", OrderType.HEAD_HUNT, OrderType.DEFAULT).list();
    }
}