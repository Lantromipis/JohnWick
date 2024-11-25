package ru.ifmo.se.johnwick.service.impl;

import cz.jirutka.rsql.parser.ast.Node;
import io.quarkus.scheduler.Scheduled;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.SecurityContext;
import ru.ifmo.se.johnwick.exception.EntityNotFoundByIdException;
import ru.ifmo.se.johnwick.exception.ValidationException;
import ru.ifmo.se.johnwick.mapper.OrderMapper;
import ru.ifmo.se.johnwick.model.OrderStatus;
import ru.ifmo.se.johnwick.model.UserRole;
import ru.ifmo.se.johnwick.model.dto.HeadHuntOrderDto;
import ru.ifmo.se.johnwick.model.entity.HeadHuntOrderEntity;
import ru.ifmo.se.johnwick.model.entity.UserEntity;
import ru.ifmo.se.johnwick.properties.BabaYagaProperties;
import ru.ifmo.se.johnwick.repository.HeadHuntOrderRepository;
import ru.ifmo.se.johnwick.repository.UserRepository;
import ru.ifmo.se.johnwick.rsql.JpaRsqlVisitorParams;
import ru.ifmo.se.johnwick.rsql.visitor.HeadHauntOrderEntityJpaRsqlVisitor;
import ru.ifmo.se.johnwick.service.api.CleaningRequestService;
import ru.ifmo.se.johnwick.service.api.HeadHuntOrderService;
import ru.ifmo.se.johnwick.service.api.NotificationService;
import ru.ifmo.se.johnwick.utils.RsqlParserUtils;

import java.time.OffsetDateTime;
import java.util.List;

@ApplicationScoped
public class HeadHuntOrderServiceImpl implements HeadHuntOrderService {

    @Inject
    HeadHuntOrderRepository headHuntOrderRepository;

    @Inject
    OrderMapper orderMapper;

    @Inject
    RsqlParserUtils rsqlParserUtils;

    @Inject
    NotificationService notificationService;

    @Inject
    BabaYagaProperties babaYagaProperties;

    @Inject
    UserRepository userRepository;

    @Context
    SecurityContext securityContext;

    @Inject
    CleaningRequestService cleaningRequestService;

    @Transactional
    @Scheduled(every = "{baba-yaga.head-hunt-order.price-increase-interval}")
    public void increaseIncompleteOrdersPrices() {
        headHuntOrderRepository.increaseIncompleteOrdersPrice(babaYagaProperties.headHuntOrder().priceIncreaseFactor());
    }

    @Override
    @Transactional
    public HeadHuntOrderDto createHeadHuntOrder(HeadHuntOrderDto headHuntOrderDto) {
        HeadHuntOrderEntity orderEntity = orderMapper.mapHeadHuntToEntity(headHuntOrderDto);

        orderEntity.setSucceededKiller(null);
        orderEntity.setId(null);
        orderEntity.setStatus(OrderStatus.AWAITING_SUBMISSION);
        orderEntity.setCreatedTimestamp(OffsetDateTime.now());
        headHuntOrderRepository.persist(orderEntity);

        notificationService.sendNotificationToAllUsersByRole(
                UserRole.KILLER,
                "New head haunt order",
                "New head haunt with id " + orderEntity.getId().toString() + " created! Be first to complete it!"
        );
        return orderMapper.mapHeadHuntToDto(orderEntity);
    }

    @Override
    public List<HeadHuntOrderDto> listHeadHuntOrders(String rsqlPredicate) {
        EntityManager em = headHuntOrderRepository.getEntityManager();
        Predicate predicate = null;

        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<HeadHuntOrderEntity> query = criteriaBuilder.createQuery(HeadHuntOrderEntity.class);
        Root<HeadHuntOrderEntity> root = query.from(HeadHuntOrderEntity.class);
        CriteriaQuery<HeadHuntOrderEntity> select = query.select(root);

        if (rsqlPredicate != null && !rsqlPredicate.isEmpty()) {
            JpaRsqlVisitorParams<HeadHuntOrderEntity> jpaRsqlVisitorParams = new JpaRsqlVisitorParams<>(
                    criteriaBuilder,
                    query,
                    root,
                    select
            );

            Node rootRsqlNode = rsqlParserUtils.parsePredicate(rsqlPredicate);
            predicate = rootRsqlNode.accept(new HeadHauntOrderEntityJpaRsqlVisitor(), jpaRsqlVisitorParams);
        }

        if (predicate != null) {
            select = select.where(predicate);
        }

        select.orderBy(criteriaBuilder.desc(root.get("createdTimestamp")));

        TypedQuery<HeadHuntOrderEntity> typedQuery = em.createQuery(select);

        typedQuery.setMaxResults(1000);
        typedQuery.setFirstResult(0);
        List<HeadHuntOrderEntity> entities = typedQuery.getResultList();
        return orderMapper.mapHeadHuntToDto(entities);
    }

    @Override
    @Transactional
    public HeadHuntOrderDto updateHeadHuntOrder(HeadHuntOrderDto headHuntOrderDto) {
        HeadHuntOrderEntity headHuntOrderEntity = headHuntOrderRepository.findById(headHuntOrderDto.getId());
        if (headHuntOrderEntity == null) {
            throw new EntityNotFoundByIdException("headHuntOrder", headHuntOrderEntity.getId().toString());
        }

        // change status
        if (headHuntOrderDto.getStatus() != null) {
            OrderStatus newStatus = headHuntOrderDto.getStatus();
            OrderStatus oldStatus = headHuntOrderEntity.getStatus();

            UserEntity currentUser = userRepository.findByUsername(securityContext.getUserPrincipal().getName());

            switch (newStatus) {
                case AWAITING_CLEANING -> {
                    if (oldStatus != OrderStatus.AWAITING_SUBMISSION) {
                        throw new ValidationException("Order in current status can not be transitioned to status AWAITING_CLEANING");
                    }

                    if (!currentUser.getRole().equals(UserRole.KILLER)) {
                        throw new ValidationException("Only killer can complete order");
                    }

                    cleaningRequestService.createCleaningForOrder(headHuntOrderEntity, currentUser);
                    headHuntOrderEntity.setStatus(OrderStatus.AWAITING_CLEANING);
                    headHuntOrderEntity.setSucceededKiller(currentUser);
                }
                case COMPLETED -> {
                    if (oldStatus != OrderStatus.AWAITING_APPROVAL) {
                        throw new ValidationException("Order in current status can not be transitioned to status COMPLETED");
                    }
                    if (!UserRole.ADMIN.equals(currentUser.getRole())) {
                        throw new ValidationException("Current user has no permissions to complete order");
                    }
                    notificationService.sendNotificationToUser(
                            headHuntOrderEntity.getSucceededKiller(),
                            "You are the successor of head hunt order ",
                            "Administrator reviewed head hunt order with id " + headHuntOrderEntity.getId() + " and concluded that it is completed. You are now a head hunt order successor. Congratulations!"
                    );
                    headHuntOrderEntity.setStatus(OrderStatus.COMPLETED);
                }
                default -> throw new ValidationException("Invalid target order status");
            }
        }

        headHuntOrderRepository.persistAndFlush(headHuntOrderEntity);
        return orderMapper.mapHeadHuntToDto(headHuntOrderEntity);
    }
}
