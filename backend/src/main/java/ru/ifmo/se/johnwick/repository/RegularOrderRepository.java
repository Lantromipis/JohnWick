package ru.ifmo.se.johnwick.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import ru.ifmo.se.johnwick.model.OrderStatus;
import ru.ifmo.se.johnwick.model.entity.RegularOrderEntity;

import java.time.OffsetDateTime;
import java.util.UUID;

@ApplicationScoped
public class RegularOrderRepository implements PanacheRepositoryBase<RegularOrderEntity, UUID> {
    public int cancelOrdersWithoutApplications(OffsetDateTime cancelBeforeTimestamp) {
        return update(
                "UPDATE RegularOrderEntity o SET status = ?1 " +
                        "WHERE o.assignee IS NULL " +
                        "AND status != ?1 " +
                        "AND o.createdTimestamp < ?2 " +
                        "AND (SELECT COUNT(*) FROM RegularOrderApplicationEntity r WHERE o = r.regularOrder) = 0",
                OrderStatus.CANCELLED,
                cancelBeforeTimestamp
        );
    }
}
