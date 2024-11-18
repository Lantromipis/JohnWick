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
import ru.ifmo.se.johnwick.exception.EntityNotFoundByIdException;
import ru.ifmo.se.johnwick.mapper.OrderMapper;
import ru.ifmo.se.johnwick.model.OrderStatus;
import ru.ifmo.se.johnwick.model.dto.PromissoryNoteOrderDto;
import ru.ifmo.se.johnwick.model.entity.PromissoryNoteOrderEntity;
import ru.ifmo.se.johnwick.model.entity.UserEntity;
import ru.ifmo.se.johnwick.repository.PromissoryNoteOrderRepository;
import ru.ifmo.se.johnwick.repository.UserRepository;
import ru.ifmo.se.johnwick.rsql.JpaRsqlVisitorParams;
import ru.ifmo.se.johnwick.rsql.visitor.PromissoryNoteOrderEntityJpaRsqlVisitor;
import ru.ifmo.se.johnwick.service.api.NotificationService;
import ru.ifmo.se.johnwick.service.api.PromissoryNoteOrderService;
import ru.ifmo.se.johnwick.utils.RsqlParserUtils;

import java.time.OffsetDateTime;
import java.util.List;

@ApplicationScoped
public class PromissoryNoteOrderServiceImpl implements PromissoryNoteOrderService {


    @Inject
    PromissoryNoteOrderRepository promissoryNoteOrderRepository;

    @Inject
    OrderMapper orderMapper;

    @Inject
    RsqlParserUtils rsqlParserUtils;

    @Inject
    NotificationService notificationService;

    @Inject
    UserRepository userRepository;

    @Override
    @Transactional
    public PromissoryNoteOrderDto createPromissoryNoteOrder(PromissoryNoteOrderDto promissoryNoteOrderDto) {
        PromissoryNoteOrderEntity orderEntity = orderMapper.mapPromissoryNoteToEntity(promissoryNoteOrderDto);

        UserEntity debtor = userRepository.findById(orderEntity.getDebtor().getId());
        if (debtor == null) {
            throw new EntityNotFoundByIdException("user", orderEntity.getDebtor().getId().toString());
        }

        UserEntity beneficiary = userRepository.findById(orderEntity.getBeneficiary().getId());
        if (beneficiary == null) {
            throw new EntityNotFoundByIdException("user", orderEntity.getBeneficiary().getId().toString());
        }

        orderEntity.setId(null);
        orderEntity.setStatus(OrderStatus.AWAITING_ASSIGNEE);
        orderEntity.setCreatedTimestamp(OffsetDateTime.now());
        orderEntity.setDebtor(debtor);
        orderEntity.setBeneficiary(beneficiary);

        promissoryNoteOrderRepository.persist(orderEntity);

        notificationService.sendNotificationToUser(
                debtor,
                "New promissory note order",
                "You were assigned to new promissory note order with id" + orderEntity.getId().toString() + "!"
        );
        return orderMapper.mapPromissoryNoteToDto(orderEntity);
    }

    @Override
    public List<PromissoryNoteOrderDto> listPromissoryNoteOrders(String rsqlPredicate) {
        EntityManager em = promissoryNoteOrderRepository.getEntityManager();
        Predicate predicate = null;

        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<PromissoryNoteOrderEntity> query = criteriaBuilder.createQuery(PromissoryNoteOrderEntity.class);
        Root<PromissoryNoteOrderEntity> root = query.from(PromissoryNoteOrderEntity.class);
        CriteriaQuery<PromissoryNoteOrderEntity> select = query.select(root);

        if (rsqlPredicate != null && !rsqlPredicate.isEmpty()) {
            JpaRsqlVisitorParams<PromissoryNoteOrderEntity> jpaRsqlVisitorParams = new JpaRsqlVisitorParams<>(
                    criteriaBuilder,
                    query,
                    root,
                    select
            );

            Node rootRsqlNode = rsqlParserUtils.parsePredicate(rsqlPredicate);
            predicate = rootRsqlNode.accept(new PromissoryNoteOrderEntityJpaRsqlVisitor(), jpaRsqlVisitorParams);
        }

        if (predicate != null) {
            select = select.where(predicate);
        }

        select.orderBy(criteriaBuilder.desc(root.get("createdTimestamp")));

        TypedQuery<PromissoryNoteOrderEntity> typedQuery = em.createQuery(select);

        typedQuery.setMaxResults(1000);
        typedQuery.setFirstResult(0);
        List<PromissoryNoteOrderEntity> entities = typedQuery.getResultList();
        return orderMapper.mapPromissoryNoteToDto(entities);
    }
}
