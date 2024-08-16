package ru.ifmo.se.johnwick.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import ru.ifmo.se.johnwick.entity.OrderEntity;
import ru.ifmo.se.johnwick.entity.UserEntity;
import ru.ifmo.se.johnwick.model.OrderType;

import java.time.Duration;
import java.util.Collection;

@ApplicationScoped
public class OrderRepository implements PanacheRepository<OrderEntity> {
    public Collection<OrderEntity> findNotCanceled() {
        return find("canceled = false").list();
    }

    public Collection<OrderEntity> findByAssignee(UserEntity assignee) {
        return find("canceled = false and assignee = ?1", assignee).list();
    }

    public Collection<OrderEntity> findAvailableOrders() {
        return find("canceled = false and ( type = ?1 or (type = ?2 and assignee is null) )", OrderType.HEAD_HUNT, OrderType.REGULAR).list();
    }

    public OrderEntity cancelOrderById(long id) {
        update("canceled = true where id = ?1", id);
        return findById(id);
    }

    public int cancelRegularOrdersWithoutApplicationsOlderThan(Duration minAge) {
        return update("canceled = true " +
                        "where canceled = false and cast(count_order_applications_by_order_id(id) as long) = 0 " +
                        "and type = ?1 and (current_timestamp - createdTimestamp) > ?2",
                OrderType.REGULAR, minAge);
    }

    public int increaseHeadHuntsPrice(int factor) {
        return update("price = price * ?1 " +
                        "where canceled = false and type = ?2",
                factor, OrderType.HEAD_HUNT);
    }
}
