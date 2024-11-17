package ru.ifmo.se.johnwick.service.impl;

import cz.jirutka.rsql.parser.ast.Node;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;
import ru.ifmo.se.johnwick.mapper.OrderMapper;
import ru.ifmo.se.johnwick.model.OrderStatus;
import ru.ifmo.se.johnwick.model.UserRole;
import ru.ifmo.se.johnwick.model.dto.HeadHuntOrderDto;
import ru.ifmo.se.johnwick.model.entity.HeadHuntOrderEntity;
import ru.ifmo.se.johnwick.repository.HeadHuntOrderRepository;
import ru.ifmo.se.johnwick.rsql.JpaRsqlVisitorParams;
import ru.ifmo.se.johnwick.rsql.visitor.HeadHauntOrderEntityJpaRsqlVisitor;
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

        select.orderBy(criteriaBuilder.asc(root.get("createdTimestamp")));

        TypedQuery<HeadHuntOrderEntity> typedQuery = em.createQuery(select);

        typedQuery.setMaxResults(1000);
        typedQuery.setFirstResult(0);
        List<HeadHuntOrderEntity> entities = typedQuery.getResultList();
        return orderMapper.mapHeadHuntToDto(entities);
    }
}
