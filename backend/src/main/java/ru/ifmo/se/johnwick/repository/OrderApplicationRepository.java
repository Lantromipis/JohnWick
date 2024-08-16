package ru.ifmo.se.johnwick.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import ru.ifmo.se.johnwick.entity.OrderApplicationEntity;
import ru.ifmo.se.johnwick.entity.OrderEntity;
import ru.ifmo.se.johnwick.entity.UserEntity;

import java.util.Collection;

@ApplicationScoped
public class OrderApplicationRepository implements PanacheRepository<OrderApplicationEntity> {
    public Collection<OrderApplicationEntity> findByOrder(OrderEntity order) {
        return find("order = ?1", order).list();
    }

    public long countByOrderAndAppliedKiller(OrderEntity order, UserEntity appliedKiller) {
        return find("order = ?1 and appliedKiller = ?2", order, appliedKiller).count();
    }
}
