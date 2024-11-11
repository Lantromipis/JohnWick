package ru.ifmo.se.johnwick.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import ru.ifmo.se.johnwick.entity.HeadHuntOrderEntity;
import ru.ifmo.se.johnwick.entity.OrderEntity;

@ApplicationScoped
public class HeadHuntOrderRepository implements PanacheRepository<HeadHuntOrderEntity> {
}
