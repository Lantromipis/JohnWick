package ru.ifmo.se.johnwick.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import ru.ifmo.se.johnwick.model.OrderType;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "\"order\"")
public class OrderEntity extends BasicEntity {
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private OrderType type;

    @Column(name = "created_timestamp", nullable = false, insertable = false, updatable = false)
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

    public static Collection<OrderEntity> findByAssignee(UserEntity assignee) {
        return find("canceled = false and assignee = ?1", assignee).list();
    }

    public static Collection<OrderEntity> findAvailableOrders() {
        return find("canceled = false and ( type = ?1 or (type = ?2 and assignee is null) )", OrderType.HEAD_HUNT, OrderType.REGULAR).list();
    }

    public static OrderEntity cancelOrderById(long id) {
        update("canceled = true where id = ?1", id);
        return findById(id);
    }

    public static int cancelRegularOrdersWithoutApplicationsOlderThan(Duration minAge) {
        return update("canceled = true " +
                        "where canceled = false and cast(count_order_applications_by_order_id(id) as long) = 0 " +
                        "and type = ?1 and (current_timestamp - createdTimestamp) > ?2",
                OrderType.REGULAR, minAge);
    }

    public static int increaseHeadHuntsPrice(int factor) {
        return update("price = price * ?1 " +
                "where canceled = false and type = ?2",
                factor, OrderType.HEAD_HUNT);
    }
}