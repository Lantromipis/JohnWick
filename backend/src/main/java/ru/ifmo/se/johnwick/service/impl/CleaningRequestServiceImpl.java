package ru.ifmo.se.johnwick.service.impl;

import cz.jirutka.rsql.parser.RSQLParser;
import cz.jirutka.rsql.parser.RSQLParserException;
import cz.jirutka.rsql.parser.UnknownOperatorException;
import cz.jirutka.rsql.parser.ast.Node;
import io.quarkus.security.identity.SecurityIdentity;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import jakarta.transaction.Transactional;
import ru.ifmo.se.johnwick.constant.ApiConstant;
import ru.ifmo.se.johnwick.exception.EntityNotFoundByIdException;
import ru.ifmo.se.johnwick.exception.UnsupportedRsqlOperatorException;
import ru.ifmo.se.johnwick.exception.ValidationException;
import ru.ifmo.se.johnwick.mapper.CleaningRequestMapper;
import ru.ifmo.se.johnwick.model.CleaningRequestStatus;
import ru.ifmo.se.johnwick.model.OrderStatus;
import ru.ifmo.se.johnwick.model.UserRole;
import ru.ifmo.se.johnwick.model.dto.CleaningRequestDto;
import ru.ifmo.se.johnwick.model.entity.CleaningRequestEntity;
import ru.ifmo.se.johnwick.model.entity.OrderEntity;
import ru.ifmo.se.johnwick.model.entity.RegularOrderEntity;
import ru.ifmo.se.johnwick.model.entity.UserEntity;
import ru.ifmo.se.johnwick.repository.CleaningRequestRepository;
import ru.ifmo.se.johnwick.repository.UserRepository;
import ru.ifmo.se.johnwick.rsql.JpaRsqlVisitorParams;
import ru.ifmo.se.johnwick.rsql.visitor.CleaningRequestEntityJpaRsqlVisitor;
import ru.ifmo.se.johnwick.service.api.CleaningRequestService;
import ru.ifmo.se.johnwick.service.api.NotificationService;

import java.time.OffsetDateTime;
import java.util.List;

@ApplicationScoped
public class CleaningRequestServiceImpl implements CleaningRequestService {

    @Inject
    CleaningRequestRepository cleaningRequestRepository;

    @Inject
    UserRepository userRepository;

    @Inject
    NotificationService notificationService;

    @Inject
    SecurityIdentity securityIdentity;

    @Inject
    CleaningRequestMapper cleaningRequestMapper;

    @Override
    @Transactional
    public void createCleaningForOrder(OrderEntity order, UserEntity requestedBy) {
        CleaningRequestEntity cleaningRequestEntity = new CleaningRequestEntity();

        cleaningRequestEntity.setOrder(order);
        cleaningRequestEntity.setCreatedTimestamp(OffsetDateTime.now());
        cleaningRequestEntity.setStatus(CleaningRequestStatus.CREATED);
        cleaningRequestEntity.setRequestedBy(requestedBy);

        notificationService.sendNotificationToAllUsersByRole(
                UserRole.CLEANER,
                "New cleaning request",
                "New cleaning request was created. Be the first one to accept it!"
        );

        cleaningRequestRepository.persistAndFlush(cleaningRequestEntity);
    }

    @Override
    public List<CleaningRequestDto> listCleanings(String rsqlPredicate) {
        EntityManager em = cleaningRequestRepository.getEntityManager();
        Predicate predicate = null;

        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<CleaningRequestEntity> query = criteriaBuilder.createQuery(CleaningRequestEntity.class);
        Root<CleaningRequestEntity> root = query.from(CleaningRequestEntity.class);
        CriteriaQuery<CleaningRequestEntity> select = query.select(root);

        if (rsqlPredicate != null && !rsqlPredicate.isEmpty()) {
            JpaRsqlVisitorParams<CleaningRequestEntity> jpaRsqlVisitorParams = new JpaRsqlVisitorParams<>(
                    criteriaBuilder,
                    query,
                    root,
                    select
            );

            RSQLParser rsqlParser = new RSQLParser();
            Node rootRsqlNode;
            try {
                rootRsqlNode = rsqlParser.parse(rsqlPredicate);
            } catch (RSQLParserException e) {
                Throwable cause = e.getCause();
                if (cause instanceof UnknownOperatorException unknownOperatorException) {
                    throw new UnsupportedRsqlOperatorException(unknownOperatorException.getOperator());
                }
                throw e;
            }
            predicate = rootRsqlNode.accept(new CleaningRequestEntityJpaRsqlVisitor(), jpaRsqlVisitorParams);
        }

        // security
        if (securityIdentity.getRoles().contains(ApiConstant.ROLE_CLEANER)) {
            Join<RegularOrderEntity, UserEntity> assignee = root.join("appliedCleaner", JoinType.LEFT);
            Predicate currentCleanerIsAssignee = criteriaBuilder.equal(assignee.get("username"), securityIdentity.getPrincipal().getName());
            Predicate statusIsCreated = criteriaBuilder.equal(root.get("status"), CleaningRequestStatus.CREATED);
            Predicate orPredicate = criteriaBuilder.or(currentCleanerIsAssignee, statusIsCreated);

            if (predicate != null) {
                predicate = criteriaBuilder.and(predicate, orPredicate);
            } else {
                predicate = orPredicate;
            }
        }

        if (predicate != null) {
            select = select.where(predicate);
        }

        select.orderBy(criteriaBuilder.desc(root.get("createdTimestamp")));

        TypedQuery<CleaningRequestEntity> typedQuery = em.createQuery(select);

        typedQuery.setMaxResults(1000);
        typedQuery.setFirstResult(0);
        List<CleaningRequestEntity> entities = typedQuery.getResultList();

        return cleaningRequestMapper.mapDtoFromEntity(entities);
    }

    @Override
    @Transactional
    public CleaningRequestDto updateCleaning(CleaningRequestDto cleaningRequestDto) {
        CleaningRequestEntity cleaningRequestEntity = cleaningRequestRepository.findById(cleaningRequestDto.getId());
        if (cleaningRequestEntity == null) {
            throw new EntityNotFoundByIdException("cleaningRequest", cleaningRequestDto.getId().toString());
        }

        if (cleaningRequestDto.getStatus() != null) {
            CleaningRequestStatus newStatus = cleaningRequestDto.getStatus();
            CleaningRequestStatus oldStatus = cleaningRequestEntity.getStatus();

            switch (newStatus) {
                case IN_PROGRESS -> {
                    if (!CleaningRequestStatus.CREATED.equals(cleaningRequestEntity.getStatus())) {
                        throw new ValidationException("Cleaning request can not be transitioned to status IN_PROGRESS");
                    }
                    if (cleaningRequestEntity.getAppliedCleaner() != null) {
                        throw new ValidationException("Cleaning already assigned to another cleaner.");
                    }

                    UserEntity currentUser = userRepository.findByUsername(securityIdentity.getPrincipal().getName());
                    cleaningRequestEntity.setAppliedCleaner(currentUser);
                    cleaningRequestEntity.setStatus(CleaningRequestStatus.IN_PROGRESS);
                }
                case COMPLETED -> {
                    if (!CleaningRequestStatus.IN_PROGRESS.equals(oldStatus)) {
                        throw new ValidationException("Cleaning request can not be transitioned to status COMPLETED");
                    }

                    UserEntity currentUser = userRepository.findByUsername(securityIdentity.getPrincipal().getName());
                    if (!currentUser.getId().equals(cleaningRequestEntity.getAppliedCleaner().getId())) {
                        throw new ValidationException("Only applied cleaner can complete cleaning request");
                    }

                    cleaningRequestEntity.setStatus(CleaningRequestStatus.COMPLETED);
                    cleaningRequestEntity.getOrder().setStatus(OrderStatus.AWAITING_APPROVAL);
                    notificationService.sendNotificationToUser(
                            cleaningRequestEntity.getRequestedBy(),
                            "Cleaning completed",
                            "Cleaning for order with id " + cleaningRequestEntity.getOrder().getId() + " is completed! You order now will be reviewed by administrator."
                    );
                }
                default -> {
                    throw new ValidationException("Invalid target cleaning request status");
                }
            }
        }

        cleaningRequestRepository.persistAndFlush(cleaningRequestEntity);
        return cleaningRequestMapper.mapDtoFromEntity(cleaningRequestEntity);
    }
}
