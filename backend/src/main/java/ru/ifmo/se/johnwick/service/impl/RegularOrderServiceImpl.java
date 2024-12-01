package ru.ifmo.se.johnwick.service.impl;

import com.cronutils.utils.StringUtils;
import cz.jirutka.rsql.parser.RSQLParser;
import cz.jirutka.rsql.parser.RSQLParserException;
import cz.jirutka.rsql.parser.UnknownOperatorException;
import cz.jirutka.rsql.parser.ast.Node;
import io.quarkus.scheduler.Scheduled;
import io.quarkus.security.identity.SecurityIdentity;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import ru.ifmo.se.johnwick.constant.ApiConstant;
import ru.ifmo.se.johnwick.exception.EntityNotFoundByIdException;
import ru.ifmo.se.johnwick.exception.UnsupportedRsqlOperatorException;
import ru.ifmo.se.johnwick.exception.ValidationException;
import ru.ifmo.se.johnwick.mapper.OrderMapper;
import ru.ifmo.se.johnwick.model.OrderStatus;
import ru.ifmo.se.johnwick.model.UserRole;
import ru.ifmo.se.johnwick.model.dto.RegularOrderApplicationDto;
import ru.ifmo.se.johnwick.model.dto.RegularOrderDto;
import ru.ifmo.se.johnwick.model.entity.RegularOrderApplicationEntity;
import ru.ifmo.se.johnwick.model.entity.RegularOrderEntity;
import ru.ifmo.se.johnwick.model.entity.UserEntity;
import ru.ifmo.se.johnwick.properties.BabaYagaProperties;
import ru.ifmo.se.johnwick.repository.RegularOrderApplicationRepository;
import ru.ifmo.se.johnwick.repository.RegularOrderRepository;
import ru.ifmo.se.johnwick.repository.UserRepository;
import ru.ifmo.se.johnwick.rsql.JpaRsqlVisitorParams;
import ru.ifmo.se.johnwick.rsql.visitor.RegularOrderApplicationEntityJpaRsqlVisitor;
import ru.ifmo.se.johnwick.rsql.visitor.RegularOrderEntityJpaRsqlVisitor;
import ru.ifmo.se.johnwick.service.api.CleaningRequestService;
import ru.ifmo.se.johnwick.service.api.NotificationService;
import ru.ifmo.se.johnwick.service.api.RegularOrderService;
import ru.ifmo.se.johnwick.utils.RsqlParserUtils;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Slf4j
@ApplicationScoped
public class RegularOrderServiceImpl implements RegularOrderService {

    @Inject
    SecurityIdentity securityIdentity;

    @Inject
    RegularOrderApplicationRepository regularOrderApplicationRepository;

    @Inject
    RegularOrderRepository regularOrderRepository;

    @Inject
    UserRepository userRepository;

    @Inject
    RsqlParserUtils rsqlParserUtils;

    @Inject
    OrderMapper orderMapper;

    @Inject
    NotificationService notificationService;

    @Inject
    BabaYagaProperties babaYagaProperties;

    @Inject
    CleaningRequestService cleaningRequestService;

    @Transactional
    @Scheduled(every = "{baba-yaga.regular-order.cancellation-interval}")
    public void cancelOrdersWithoutApplications() {
        OffsetDateTime now = OffsetDateTime.now();
        OffsetDateTime cancelBeforeTimestamp = now.minus(babaYagaProperties.regularOrder().maxAgeForCancellation());
        int canceledCount = regularOrderRepository.cancelOrdersWithoutApplications(cancelBeforeTimestamp);
        if (canceledCount > 0) {
            log.info("Cancelled {} regular orders without applications", canceledCount);
        }
    }

    @Override
    @Transactional
    public RegularOrderDto createRegularOrder(RegularOrderDto regularOrderDto) {
        if (StringUtils.isEmpty(regularOrderDto.getTargetName())) {
            throw new ValidationException("Target name is required");
        }
        if (StringUtils.isEmpty(regularOrderDto.getCustomerName())) {
            throw new ValidationException("Customer name is required");
        }
        if (regularOrderDto.getPrice() <= 0) {
            throw new ValidationException("Price must be greater than 0");
        }

        RegularOrderEntity orderEntity = orderMapper.mapRegularToEntity(regularOrderDto);

        orderEntity.setApplications(null);
        orderEntity.setAssignee(null);
        orderEntity.setStatus(OrderStatus.AWAITING_APPLICATIONS);
        orderEntity.setId(null);
        orderEntity.setCreatedTimestamp(OffsetDateTime.now());

        regularOrderRepository.persist(orderEntity);

        notificationService.sendNotificationToAllUsersByRole(
                UserRole.KILLER,
                "New regular order",
                "New regular order with id " + orderEntity.getId().toString() + " created! Apply for it now!"
        );
        return orderMapper.mapRegularToDto(orderEntity);
    }

    @Override
    public RegularOrderDto getRegularOrder(UUID orderId) {
        RegularOrderEntity regularOrderEntity = regularOrderRepository.findById(orderId);
        if (regularOrderEntity == null) {
            throw new EntityNotFoundByIdException("regularOrder", orderId.toString());
        }

        return orderMapper.mapRegularToDto(regularOrderEntity);
    }

    @Override
    public List<RegularOrderDto> listRegularOrders(String rsqlPredicate) {
        EntityManager em = regularOrderRepository.getEntityManager();
        Predicate predicate = null;

        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<RegularOrderEntity> query = criteriaBuilder.createQuery(RegularOrderEntity.class);
        Root<RegularOrderEntity> root = query.from(RegularOrderEntity.class);
        CriteriaQuery<RegularOrderEntity> select = query.select(root);

        if (rsqlPredicate != null && !rsqlPredicate.isEmpty()) {
            JpaRsqlVisitorParams<RegularOrderEntity> jpaRsqlVisitorParams = new JpaRsqlVisitorParams<>(
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
            predicate = rootRsqlNode.accept(new RegularOrderEntityJpaRsqlVisitor(), jpaRsqlVisitorParams);
        }

        // security
        if (securityIdentity.getRoles().contains(ApiConstant.ROLE_KILLER)) {
            Join<RegularOrderEntity, UserEntity> assignee = root.join("assignee", JoinType.LEFT);
            Predicate currentKillerIsAssignee = criteriaBuilder.equal(assignee.get("username"), securityIdentity.getPrincipal().getName());
            Predicate statusIsAwaitingApplications = criteriaBuilder.equal(root.get("status"), OrderStatus.AWAITING_APPLICATIONS);
            Predicate orPredicate = criteriaBuilder.or(currentKillerIsAssignee, statusIsAwaitingApplications);

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

        TypedQuery<RegularOrderEntity> typedQuery = em.createQuery(select);

        typedQuery.setMaxResults(1000);
        typedQuery.setFirstResult(0);
        List<RegularOrderEntity> entities = typedQuery.getResultList();

        return orderMapper.mapRegularToDtoWithoutApplications(entities);
    }

    @Override
    public List<RegularOrderApplicationDto> listRegularOrderApplications(String rsqlPredicate) {
        EntityManager em = regularOrderApplicationRepository.getEntityManager();
        Predicate predicate = null;

        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<RegularOrderApplicationEntity> query = criteriaBuilder.createQuery(RegularOrderApplicationEntity.class);
        Root<RegularOrderApplicationEntity> root = query.from(RegularOrderApplicationEntity.class);
        CriteriaQuery<RegularOrderApplicationEntity> select = query.select(root);

        if (rsqlPredicate != null && !rsqlPredicate.isEmpty()) {
            JpaRsqlVisitorParams<RegularOrderApplicationEntity> jpaRsqlVisitorParams = new JpaRsqlVisitorParams<>(
                    criteriaBuilder,
                    query,
                    root,
                    select
            );

            Node rootRsqlNode = rsqlParserUtils.parsePredicate(rsqlPredicate);
            predicate = rootRsqlNode.accept(new RegularOrderApplicationEntityJpaRsqlVisitor(), jpaRsqlVisitorParams);
        }

        // security
        if (securityIdentity.getRoles().contains(ApiConstant.ROLE_KILLER)) {
            Predicate currentKillerApplicationsPredicate = criteriaBuilder.equal(root.get("killer").get("username"), securityIdentity.getPrincipal().getName());
            if (predicate != null) {
                predicate = criteriaBuilder.and(predicate, currentKillerApplicationsPredicate);
            } else {
                predicate = currentKillerApplicationsPredicate;
            }
        }

        if (predicate != null) {
            select = select.where(predicate);
        }

        select.orderBy(criteriaBuilder.desc(root.get("createdTimestamp")));

        TypedQuery<RegularOrderApplicationEntity> typedQuery = em.createQuery(select);

        typedQuery.setMaxResults(1000);
        typedQuery.setFirstResult(0);
        List<RegularOrderApplicationEntity> entities = typedQuery.getResultList();
        return orderMapper.mapRegularOrderApplicationToDto(entities);
    }

    @Override
    @Transactional
    public RegularOrderApplicationDto createRegularOrderApplication(UUID orderId) {
        RegularOrderEntity regularOrderEntity = regularOrderRepository.findById(orderId);
        if (regularOrderEntity == null) {
            throw new EntityNotFoundByIdException("regularOrder", orderId.toString());
        }

        if (!OrderStatus.AWAITING_APPLICATIONS.equals(regularOrderEntity.getStatus())) {
            throw new ValidationException("Order is not in AWAITING_APPLICATIONS status");
        }

        UserEntity userEntity = userRepository.findByUsername(securityIdentity.getPrincipal().getName());
        if (!UserRole.KILLER.equals(userEntity.getRole())) {
            throw new ValidationException("Assignee has no role KILLER");
        }

        RegularOrderApplicationEntity existingOrderApplication = regularOrderApplicationRepository.findByOrderAndUser(regularOrderEntity, userEntity);
        if (existingOrderApplication != null) {
            return orderMapper.mapRegularOrderApplicationToDto(existingOrderApplication);
        }

        RegularOrderApplicationEntity regularOrderApplicationEntity = new RegularOrderApplicationEntity();
        regularOrderApplicationEntity.setId(null);
        regularOrderApplicationEntity.setCreatedTimestamp(OffsetDateTime.now());
        regularOrderApplicationEntity.setRegularOrder(regularOrderEntity);
        regularOrderApplicationEntity.setKiller(userEntity);

        regularOrderApplicationRepository.persistAndFlush(regularOrderApplicationEntity);
        return orderMapper.mapRegularOrderApplicationToDto(regularOrderApplicationEntity);
    }

    @Override
    @Transactional
    public RegularOrderDto updateRegularOrder(RegularOrderDto regularOrderDto) {
        RegularOrderEntity regularOrderEntity = regularOrderRepository.findById(regularOrderDto.getId());
        if (regularOrderEntity == null) {
            throw new EntityNotFoundByIdException("regularOrder", regularOrderDto.getId().toString());
        }

        UserEntity currentUser = userRepository.findByUsername(securityIdentity.getPrincipal().getName());

        // select assignee
        if (regularOrderDto.getAssignee() != null && regularOrderDto.getAssignee().getId() != null) {
            if (!UserRole.ADMIN.equals(currentUser.getRole())) {
                throw new ValidationException("Current user has no permissions to select assignee");
            }

            UserEntity selectedKillerEntity = userRepository.findById(regularOrderDto.getAssignee().getId());
            if (selectedKillerEntity == null) {
                throw new EntityNotFoundByIdException("user", regularOrderDto.getId().toString());
            }
            if (!UserRole.KILLER.equals(selectedKillerEntity.getRole())) {
                throw new ValidationException("Assignee has no role KILLER");
            }

            Set<RegularOrderApplicationEntity> existingApplications = regularOrderEntity.getApplications();
            RegularOrderApplicationEntity selectedKillerApplication = null;
            for (RegularOrderApplicationEntity existingApplication : existingApplications) {
                if (existingApplication.getKiller().getId().equals(selectedKillerEntity.getId())) {
                    selectedKillerApplication = existingApplication;
                    notificationService.sendNotificationToUser(
                            existingApplication.getKiller(),
                            "You are selected as assignee",
                            "You are selected as assignee for order with id " + regularOrderDto.getId().toString() + ". Congratulations!"
                    );
                } else {
                    notificationService.sendNotificationToUser(
                            existingApplication.getKiller(),
                            "You were not selected as assignee.",
                            "You applied for order with id " + regularOrderDto.getId().toString() + ". However, you were not selected as assignee."
                    );
                }
            }

            if (selectedKillerApplication == null) {
                throw new ValidationException("Selected assignee did not applied for this order");
            }

            regularOrderEntity.setAssignee(selectedKillerEntity);
            regularOrderEntity.setStatus(OrderStatus.AWAITING_ASSIGNEE);
            regularOrderEntity.getApplications().clear();
        }

        // change status
        if (regularOrderDto.getStatus() != null) {
            if (!currentUser.getRole().equals(UserRole.ADMIN) && !regularOrderEntity.getAssignee().getId().equals(currentUser.getId())) {
                throw new ValidationException("Current user have no permission to modify this order");
            }

            OrderStatus newStatus = regularOrderDto.getStatus();
            OrderStatus oldStatus = regularOrderEntity.getStatus();

            switch (newStatus) {
                case AWAITING_SUIT -> {
                    if (oldStatus != OrderStatus.AWAITING_ASSIGNEE) {
                        throw new ValidationException("Order in current status can not be transitioned to status AWAITING_SUIT");
                    }
                    regularOrderEntity.setStatus(OrderStatus.AWAITING_SUIT);
                }
                case AWAITING_DEGUSTATION -> {
                    if (oldStatus != OrderStatus.AWAITING_SUIT) {
                        throw new ValidationException("Order in current status can not be transitioned to status AWAITING_DEGUSTATION");
                    }
                    regularOrderEntity.setStatus(OrderStatus.AWAITING_DEGUSTATION);
                }
                case AWAITING_SUBMISSION -> {
                    if (oldStatus != OrderStatus.AWAITING_DEGUSTATION) {
                        throw new ValidationException("Order in current status can not be transitioned to status AWAITING_SUBMISSION");
                    }
                    regularOrderEntity.setStatus(OrderStatus.AWAITING_SUBMISSION);
                }
                case AWAITING_CLEANING -> {
                    if (oldStatus != OrderStatus.AWAITING_SUBMISSION) {
                        throw new ValidationException("Order in current status can not be transitioned to status AWAITING_CLEANING");
                    }
                    if (!currentUser.getRole().equals(UserRole.KILLER)) {
                        throw new ValidationException("Only killer can complete order");
                    }

                    cleaningRequestService.createCleaningForOrder(regularOrderEntity, currentUser);
                    regularOrderEntity.setStatus(OrderStatus.AWAITING_CLEANING);
                }
                case COMPLETED -> {
                    if (oldStatus != OrderStatus.AWAITING_APPROVAL) {
                        throw new ValidationException("Order in current status can not be transitioned to status COMPLETED");
                    }
                    if (!UserRole.ADMIN.equals(currentUser.getRole())) {
                        throw new ValidationException("Current user has no permissions to complete order");
                    }
                    notificationService.sendNotificationToUser(
                            regularOrderEntity.getAssignee(),
                            "Your regular order is marked as completed",
                            "Administrator reviewed your regular order with id " + regularOrderEntity.getId() + " and concluded that it is completed. Congratulations!"
                    );
                    regularOrderEntity.setStatus(OrderStatus.COMPLETED);
                }
                default -> throw new ValidationException("Invalid target order status");
            }
        }

        regularOrderRepository.persistAndFlush(regularOrderEntity);
        return orderMapper.mapRegularToDto(regularOrderEntity);
    }
}
