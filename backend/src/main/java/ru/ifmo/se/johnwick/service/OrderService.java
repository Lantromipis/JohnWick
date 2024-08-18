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
import ru.ifmo.se.johnwick.mapper.OrderApplicationMapper;
import ru.ifmo.se.johnwick.mapper.OrderMapper;
import ru.ifmo.se.johnwick.model.OrderType;
import ru.ifmo.se.johnwick.model.dto.AvailableOrderDto;
import ru.ifmo.se.johnwick.model.dto.OrderApplicationDto;
import ru.ifmo.se.johnwick.model.dto.OrderDto;
import ru.ifmo.se.johnwick.model.input.HeadHuntOrderInput;
import ru.ifmo.se.johnwick.model.input.OrderInput;
import ru.ifmo.se.johnwick.model.input.PromissoryNoteOrderInput;
import ru.ifmo.se.johnwick.model.input.RegularOrderInput;
import ru.ifmo.se.johnwick.repository.OrderApplicationRepository;
import ru.ifmo.se.johnwick.repository.OrderRepository;

import java.time.Duration;
import java.util.Collection;

@ApplicationScoped
public class OrderService {
    @Inject
    Logger LOG;

    @Inject
    OrderMapper orderMapper;

    @Inject
    OrderApplicationMapper orderApplicationMapper;

    @Inject
    NotificationService notificationService;

    @Inject
    UserService userService;

    @Inject
    OrderApplicationRepository orderApplicationRepository;

    @Inject
    OrderRepository orderRepository;

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
        int count = orderRepository.cancelRegularOrdersWithoutApplicationsOlderThan(regularOrdersMaxAgeDuration);
        LOG.info("canceled " + count + " regular orders without applications");
    }

    @Transactional
    @Scheduled(every = "{johnwick.head-hunt.price-increase-interval}")
    void increaseHeadHuntsPrice() {
        int count = orderRepository.increaseHeadHuntsPrice(headHuntPrintIncreaseFactor);
        LOG.info("increased " + count + " head hunts prices");
    }

    public OrderDto createOrder(OrderInput orderInput) {
        OrderEntity entity = switch (orderInput.getType()) {
            case REGULAR -> orderMapper.mapInputToEntity((RegularOrderInput) orderInput);
            case PROMISSORY_NOTE -> orderMapper.mapInputToEntity((PromissoryNoteOrderInput) orderInput);
            case HEAD_HUNT -> orderMapper.mapInputToEntity((HeadHuntOrderInput) orderInput);
        };
        entity.persist();

        OrderDto orderDto = orderMapper.entityToDto(entity);
        notificationService.notifyAboutOrderCreation(orderDto);

        return orderMapper.entityToDto(entity);
    }

    public Collection<OrderDto> getActiveOrders() {
        return orderMapper.entitiesToDtos(orderRepository.findNotCanceled());
    }

    public Collection<OrderDto> getUserAssignedOrders(String username) {
        UserEntity assignee = userService.getUserEntity(username);
        Collection<OrderEntity> assignedOrders = orderRepository.findByAssignee(assignee);
        return orderMapper.entitiesToDtos(assignedOrders);
    }

    public Collection<AvailableOrderDto> getUserAvailableOrders(String username) {
        Collection<OrderEntity> availableOrders = orderRepository.findAvailableOrders();
        return orderMapper.entitiesToAvailableDtos(availableOrders, username);
    }

    public OrderApplicationDto createRegularOrderApplication(long orderId, String username) {
        OrderEntity orderEntity = orderRepository.findById(orderId);
        if (!orderEntity.getType().equals(OrderType.REGULAR)) {
            throw new IllegalArgumentException();
        }

        UserEntity userEntity = userService.getUserEntity(username);
        OrderApplicationEntity orderApplicationEntity = new OrderApplicationEntity(userEntity, orderEntity);
        orderApplicationEntity.persist();

        return orderApplicationMapper.entityToDto(orderApplicationEntity);
    }

    public Collection<OrderApplicationDto> getOrderApplications(long orderId) {
        OrderEntity orderEntity = OrderEntity.findById(orderId);
        Collection<OrderApplicationEntity> applications = orderApplicationRepository.findByOrder(orderEntity);
        return orderApplicationMapper.entitiesToDtos(applications);
    }

    public OrderDto chooseOrderApplication(long applicationId) {
        OrderApplicationEntity orderApplicationEntity = orderApplicationRepository.findById(applicationId);
        OrderEntity orderEntity = orderApplicationEntity.getOrder();
        UserEntity userEntity = orderApplicationEntity.getAppliedKiller();

        orderEntity.setAssignee(userEntity);

        notificationService.notifyAboutAcceptedApplication(orderApplicationMapper.entityToDto(orderApplicationEntity));

        return orderMapper.entityToDto(orderEntity);
    }

    public OrderDto cancelOrder(long orderId) {
        OrderEntity orderEntity = orderRepository.cancelOrderById(orderId);
        return orderMapper.entityToDto(orderEntity);
    }

    public boolean hasKillerAppliedToOrder(OrderEntity order, String killerUsername) {
        UserEntity killer = userService.getUserEntity(killerUsername);
        return orderApplicationRepository.countByOrderAndAppliedKiller(order, killer) > 0;
    }
}
