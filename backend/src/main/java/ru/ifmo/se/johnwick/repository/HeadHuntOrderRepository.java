package ru.ifmo.se.johnwick.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import ru.ifmo.se.johnwick.model.OrderStatus;
import ru.ifmo.se.johnwick.model.entity.HeadHuntOrderEntity;

import java.util.UUID;

@ApplicationScoped
public class HeadHuntOrderRepository implements PanacheRepositoryBase<HeadHuntOrderEntity, UUID> {
    public int increaseIncompleteOrdersPrice(double factor) {
        return update("UPDATE HeadHuntOrderEntity e SET currentPrice = cast((cast(currentPrice as double) * ?1 + currentPrice + 1) as long) WHERE status = ?2",
                factor,
                OrderStatus.AWAITING_SUBMISSION
        );
    }
}
