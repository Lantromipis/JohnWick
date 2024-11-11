package ru.ifmo.se.johnwick.repository;


import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import ru.ifmo.se.johnwick.entity.OrderEntity;
import ru.ifmo.se.johnwick.entity.RegularOrderEntity;

@ApplicationScoped
public class RegularOrderRepository implements PanacheRepository<RegularOrderEntity> {
}
