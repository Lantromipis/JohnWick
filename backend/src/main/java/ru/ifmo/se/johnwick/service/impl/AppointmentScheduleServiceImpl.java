package ru.ifmo.se.johnwick.service.impl;

import cz.jirutka.rsql.parser.ast.Node;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.SecurityContext;
import ru.ifmo.se.johnwick.constant.ApiConstant;
import ru.ifmo.se.johnwick.exception.EntityNotFoundByIdException;
import ru.ifmo.se.johnwick.exception.ValidationException;
import ru.ifmo.se.johnwick.mapper.AppointmentScheduleMapper;
import ru.ifmo.se.johnwick.model.UserRole;
import ru.ifmo.se.johnwick.model.dto.AppointmentDto;
import ru.ifmo.se.johnwick.model.dto.AppointmentScheduleDto;
import ru.ifmo.se.johnwick.model.entity.AppointmentEntity;
import ru.ifmo.se.johnwick.model.entity.AppointmentScheduleEntity;
import ru.ifmo.se.johnwick.model.entity.UserEntity;
import ru.ifmo.se.johnwick.repository.AppointmentRepository;
import ru.ifmo.se.johnwick.repository.AppointmentsScheduleRepository;
import ru.ifmo.se.johnwick.repository.UserRepository;
import ru.ifmo.se.johnwick.rsql.JpaRsqlVisitorParams;
import ru.ifmo.se.johnwick.rsql.visitor.AppointmentScheduleEntityJpaRsqlVisitor;
import ru.ifmo.se.johnwick.service.api.AppointmentScheduleService;
import ru.ifmo.se.johnwick.utils.RsqlParserUtils;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;


@ApplicationScoped
public class AppointmentScheduleServiceImpl implements AppointmentScheduleService {

    @Inject
    AppointmentsScheduleRepository appointmentsScheduleRepository;

    @Inject
    AppointmentRepository appointmentRepository;

    @Inject
    AppointmentScheduleMapper appointmentScheduleMapper;

    @Inject
    UserRepository userRepository;

    @Inject
    RsqlParserUtils rsqlParserUtils;

    @Context
    SecurityContext securityContext;

    @Override
    @Transactional
    public AppointmentScheduleDto createAppointmentSchedule(AppointmentScheduleDto appointmentScheduleDto) {
        if (appointmentScheduleDto.getStartTime().isAfter(appointmentScheduleDto.getEndTime())) {
            throw new ValidationException("Appointment schedule start time must be before end time.");
        }

        appointmentScheduleDto.setId(null);
        appointmentScheduleDto.setHost(null);
        appointmentScheduleDto.setAppointments(null);

        AppointmentScheduleEntity appointmentScheduleEntity = appointmentScheduleMapper.appointmentScheduleDtoToEntity(appointmentScheduleDto);
        UserEntity host = userRepository.findByUsername(securityContext.getUserPrincipal().getName());
        if (!UserRole.TAILOR.equals(host.getRole()) && !UserRole.SOMMELIER.equals(host.getRole())) {
            throw new ValidationException("User has no role TAILOR or SOMMELIER.");
        }

        if (appointmentsScheduleRepository.existsForRange(host, appointmentScheduleEntity.getStartTime(), appointmentScheduleEntity.getEndTime())) {
            throw new ValidationException("Existing appointment schedule for this user overlaps new schedule.");
        }

        appointmentScheduleEntity.setHost(host);
        appointmentsScheduleRepository.persistAndFlush(appointmentScheduleEntity);

        return appointmentScheduleMapper.appointmentScheduleEntityToDto(appointmentScheduleEntity);
    }

    @Override
    public List<AppointmentScheduleDto> listAppointmentSchedule(String rsqlPredicate) {
        EntityManager em = appointmentsScheduleRepository.getEntityManager();
        Predicate predicate = null;

        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<AppointmentScheduleEntity> query = criteriaBuilder.createQuery(AppointmentScheduleEntity.class);
        Root<AppointmentScheduleEntity> root = query.from(AppointmentScheduleEntity.class);
        CriteriaQuery<AppointmentScheduleEntity> select = query.select(root);

        if (rsqlPredicate != null && !rsqlPredicate.isEmpty()) {
            JpaRsqlVisitorParams<AppointmentScheduleEntity> jpaRsqlVisitorParams = new JpaRsqlVisitorParams<>(
                    criteriaBuilder,
                    query,
                    root,
                    select
            );

            Node rootRsqlNode = rsqlParserUtils.parsePredicate(rsqlPredicate);
            predicate = rootRsqlNode.accept(new AppointmentScheduleEntityJpaRsqlVisitor(), jpaRsqlVisitorParams);
        }

        if (predicate != null) {
            select = select.where(predicate);
        }

        select.orderBy(criteriaBuilder.desc(root.get("date")));

        TypedQuery<AppointmentScheduleEntity> typedQuery = em.createQuery(select);

        typedQuery.setMaxResults(1000);
        typedQuery.setFirstResult(0);
        List<AppointmentScheduleEntity> entities = typedQuery.getResultList();
        if (securityContext.isUserInRole(ApiConstant.ROLE_KILLER)) {
            return appointmentScheduleMapper.appointmentScheduleEntityToDtoWithoutBooker(entities);
        } else {
            return appointmentScheduleMapper.appointmentScheduleEntityToDto(entities);
        }
    }

    @Override
    @Transactional
    public AppointmentDto createAppointment(UUID appointmentScheduleId, AppointmentDto appointmentDto) {
        AppointmentScheduleEntity appointmentScheduleEntity = appointmentsScheduleRepository.findById(
                appointmentScheduleId,
                LockModeType.PESSIMISTIC_WRITE
        );
        if (appointmentScheduleEntity == null) {
            throw new EntityNotFoundByIdException("appointmentSchedule", appointmentScheduleId.toString());
        }

        if (!isWithinRange(appointmentDto.getStartTime(), appointmentScheduleEntity.getStartTime(), appointmentScheduleEntity.getEndTime())) {
            throw new ValidationException("Appointment start time is not in schedule.");
        }
        if (!isWithinRange(appointmentDto.getStartTime(), appointmentScheduleEntity.getStartTime(), appointmentScheduleEntity.getEndTime())) {
            throw new ValidationException("Appointment end time is not in schedule.");
        }
        if (appointmentDto.getStartTime().isAfter(appointmentScheduleEntity.getEndTime())) {
            throw new ValidationException("Appointment start time must be before end time.");
        }

        appointmentDto.setId(null);
        appointmentDto.setBookedBy(null);

        UserEntity booker = userRepository.findByUsername(securityContext.getUserPrincipal().getName());

        AppointmentEntity appointmentEntity = appointmentScheduleMapper.appointmentEntityToDto(appointmentDto);
        appointmentEntity.setAppointmentSchedule(appointmentScheduleEntity);
        appointmentEntity.setBookedBy(booker);

        appointmentRepository.persistAndFlush(appointmentEntity);
        return appointmentScheduleMapper.appointmentEntityToDto(appointmentEntity);
    }

    private boolean isWithinRange(OffsetDateTime testDate, OffsetDateTime startDate, OffsetDateTime endDate) {
        return !(testDate.isBefore(startDate) || testDate.isAfter(endDate));
    }
}
