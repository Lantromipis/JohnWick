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

import java.time.Instant;
import java.util.Collection;

@Getter
@Setter
@Entity
@Table(name = "\"order\"")
public class OrderEntity extends BasicEntity {
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private OrderType type;

    @Column(name = "created_timestamp", nullable = false)
    private Instant createdTimestamp;

    @ManyToOne
    @JoinColumn(name = "assignee_id")
    private UserEntity assignee;

    @ManyToOne
    @JoinColumn(name = "beneficiary_id")
    private UserEntity beneficiary;

    @Column(name = "customer", nullable = false)
    private String customer;

    @Column(name = "target")
    private String target;

    @Column(name = "price", nullable = false)
    private Long price;

    @Column(name = "description")
    private String description;

    @Column(name = "canceled", nullable = false)
    private Boolean canceled = false;

    public static Collection<OrderEntity> findNotCanceled() {
        return find("canceled = false").list();
    }

    public static Collection<OrderEntity> findPromissoryNotesByDebtor(UserEntity entity) {
        return find("canceled = false and type = ?1 and debtor = ?2", OrderType.PROMISSORY_NOTE, entity).list();
    }

    public static Collection<OrderEntity> findAvailableOrders() {
        return find("canceled = false and ( type = ?1 or (type = ?2 and assignee is null) )", OrderType.HEAD_HUNT, OrderType.REGULAR).list();
    }

    public static OrderEntity cancelOrderById(long id) {
        update("canceled = true where id = ?1", id);
        return findById(id);
    }
}