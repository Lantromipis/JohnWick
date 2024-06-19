package ru.ifmo.se.johnwick.service;

import io.quarkus.scheduler.Scheduled;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;
import ru.ifmo.se.johnwick.entity.OrderApplicationEntity;
import ru.ifmo.se.johnwick.entity.OrderEntity;
import ru.ifmo.se.johnwick.entity.UserEntity;

import java.time.Duration;

@ApplicationScoped
public class OrderService {
    @Inject
    Logger LOG;

    @ConfigProperty(name = "johnwick.regular-order.max-age")
    String regularOrdersMaxAge;

    @ConfigProperty(name = "johnwick.head-hunt.price-increase-factor")
    int headHuntPrintIncreaseFactor;

    Duration regularOrdersMaxAgeDuration;

    @PostConstruct
    void init() {
        regularOrdersMaxAgeDuration = Duration.parse(regularOrdersMaxAge);
    }

    @Transactional
    @Scheduled(every = "{johnwick.regular-order.cleaning-interval}")
    void cancelOldRegularOrdersWithoutApplications() {
        int count = OrderEntity.cancelRegularOrdersWithoutApplicationsOlderThan(regularOrdersMaxAgeDuration);
        LOG.info("canceled " + count + " regular orders without applications");
    }

    @Transactional
    @Scheduled(every = "{johnwick.head-hunt.price-increase-interval}")
    void increaseHeadHuntsPrice() {
        int count = OrderEntity.increaseHeadHuntsPrice(headHuntPrintIncreaseFactor);
        LOG.info("increased " + count + " head hunts prices");
    }

    public boolean hasKillerAppliedToOrder(OrderEntity order, UserEntity killer) {
        return OrderApplicationEntity.countByOrderAndAppliedKiller(order, killer) > 0;
    }
}
