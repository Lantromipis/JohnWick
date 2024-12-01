package ru.ifmo.se.johnwick.service.impl;

import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import jakarta.inject.Inject;
import jakarta.persistence.LockModeType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.ifmo.se.johnwick.constant.ApiConstant;
import ru.ifmo.se.johnwick.exception.EntityNotFoundByIdException;
import ru.ifmo.se.johnwick.exception.ValidationException;
import ru.ifmo.se.johnwick.model.UserRole;
import ru.ifmo.se.johnwick.model.dto.AppointmentDto;
import ru.ifmo.se.johnwick.model.dto.AppointmentScheduleDto;
import ru.ifmo.se.johnwick.model.entity.AppointmentEntity;
import ru.ifmo.se.johnwick.model.entity.AppointmentScheduleEntity;
import ru.ifmo.se.johnwick.model.entity.UserEntity;
import ru.ifmo.se.johnwick.repository.AppointmentRepository;
import ru.ifmo.se.johnwick.repository.AppointmentsScheduleRepository;
import ru.ifmo.se.johnwick.repository.UserRepository;
import ru.ifmo.se.johnwick.service.api.AppointmentScheduleService;
import ru.ifmo.se.johnwick.utils.RsqlParserUtils;

import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@QuarkusTest
class AppointmentScheduleServiceImplTest {

    @InjectMock
    AppointmentsScheduleRepository appointmentsScheduleRepository;

    @InjectMock
    AppointmentRepository appointmentRepository;

    @InjectMock
    UserRepository userRepository;

    @InjectMock
    RsqlParserUtils rsqlParserUtils;

    @Inject
    AppointmentScheduleService appointmentScheduleService;

    public static final String TEST_KILLER_NAME = "testKiller";
    public static final String TEST_TAILOR_NAME = "testTailor";

    private UserEntity testKillerEntity;
    private UserEntity testTailorEntity;

    @BeforeEach
    void setUp() {
        testKillerEntity = new UserEntity();
        testKillerEntity.setUsername(TEST_KILLER_NAME);
        testKillerEntity.setId(UUID.randomUUID());
        testKillerEntity.setRole(UserRole.KILLER);

        Mockito.when(userRepository.findByUsername(TEST_KILLER_NAME)).thenReturn(testKillerEntity);

        testTailorEntity = new UserEntity();
        testTailorEntity.setUsername(TEST_TAILOR_NAME);
        testTailorEntity.setId(UUID.randomUUID());
        testTailorEntity.setRole(UserRole.TAILOR);

        Mockito.when(userRepository.findByUsername(TEST_TAILOR_NAME)).thenReturn(testTailorEntity);
    }

    @Test
    @TestSecurity(user = TEST_TAILOR_NAME, roles = {ApiConstant.ROLE_TAILOR})
    void testCreateSchedule() {
        AppointmentScheduleDto appointmentScheduleDto = new AppointmentScheduleDto();
        appointmentScheduleDto.setStartTime(OffsetDateTime.parse("2025-02-11T10:00:00+03:00"));
        appointmentScheduleDto.setEndTime(OffsetDateTime.parse("2025-02-11T16:00:00+03:00"));

        Mockito.when(appointmentsScheduleRepository.existsForRange(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(false);

        AppointmentScheduleDto createScheduleDto = appointmentScheduleService.createAppointmentSchedule(appointmentScheduleDto);
        assertEquals(createScheduleDto.getHost().getId(), testTailorEntity.getId());
        assertEquals(createScheduleDto.getStartTime(), appointmentScheduleDto.getStartTime());
        assertEquals(createScheduleDto.getEndTime(), appointmentScheduleDto.getEndTime());
    }

    @Test
    @TestSecurity(user = TEST_TAILOR_NAME, roles = {ApiConstant.ROLE_TAILOR})
    void testCreateSchedulePayloadValidations() {
        AppointmentScheduleDto appointmentScheduleDto = new AppointmentScheduleDto();

        // end before start
        appointmentScheduleDto.setStartTime(OffsetDateTime.parse("2025-02-11T16:00:00+03:00"));
        appointmentScheduleDto.setEndTime(OffsetDateTime.parse("2025-02-11T10:00:00+03:00"));
        assertThrows(ValidationException.class, () -> appointmentScheduleService.createAppointmentSchedule(appointmentScheduleDto));

        // start before now
        appointmentScheduleDto.setStartTime(OffsetDateTime.parse("2024-11-11T10:00:00+03:00"));
        appointmentScheduleDto.setEndTime(OffsetDateTime.parse("2024-11-11T16:00:00+03:00"));
        assertThrows(ValidationException.class, () -> appointmentScheduleService.createAppointmentSchedule(appointmentScheduleDto));

        // interception by range
        appointmentScheduleDto.setStartTime(OffsetDateTime.parse("2025-02-11T10:00:00+03:00"));
        appointmentScheduleDto.setEndTime(OffsetDateTime.parse("2025-02-11T16:00:00+03:00"));
        Mockito.when(appointmentsScheduleRepository.existsForRange(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(true);
        assertThrows(ValidationException.class, () -> appointmentScheduleService.createAppointmentSchedule(appointmentScheduleDto));
    }

    @Test
    @TestSecurity(user = TEST_KILLER_NAME, roles = {ApiConstant.ROLE_KILLER})
    void testCreateAppointment() {
        UUID scheduleId = UUID.randomUUID();

        AppointmentScheduleEntity scheduleEntity = new AppointmentScheduleEntity();
        scheduleEntity.setId(scheduleId);
        scheduleEntity.setStartTime(OffsetDateTime.parse("2025-02-11T10:00:00+03:00"));
        scheduleEntity.setEndTime(OffsetDateTime.parse("2025-02-11T16:00:00+03:00"));

        Mockito.when(appointmentsScheduleRepository.findById(scheduleId, LockModeType.PESSIMISTIC_WRITE)).thenReturn(scheduleEntity);

        AppointmentDto appointmentToCreate = new AppointmentDto();
        appointmentToCreate.setStartTime(OffsetDateTime.parse("2025-02-11T10:00:00+03:00"));
        appointmentToCreate.setEndTime(OffsetDateTime.parse("2025-02-11T11:00:00+03:00"));

        AppointmentDto createdAppointment = appointmentScheduleService.createAppointment(scheduleId, appointmentToCreate);
        assertEquals(createdAppointment.getStartTime(), appointmentToCreate.getStartTime());
        assertEquals(createdAppointment.getEndTime(), appointmentToCreate.getEndTime());
        assertEquals(createdAppointment.getBookedBy().getId(), testKillerEntity.getId());
        assertEquals(createdAppointment.getAppointmentSchedule().getId(), scheduleId);
    }

    @Test
    @TestSecurity(user = TEST_KILLER_NAME, roles = {ApiConstant.ROLE_KILLER})
    void testCreateAppointmentPayloadValidations() {
        UUID scheduleId = UUID.randomUUID();

        AppointmentScheduleEntity scheduleEntity = new AppointmentScheduleEntity();
        scheduleEntity.setId(scheduleId);
        scheduleEntity.setStartTime(OffsetDateTime.parse("2025-02-11T10:00:00+03:00"));
        scheduleEntity.setEndTime(OffsetDateTime.parse("2025-02-11T16:00:00+03:00"));

        AppointmentDto appointmentToCreate = new AppointmentDto();
        appointmentToCreate.setStartTime(OffsetDateTime.parse("2025-02-11T10:00:00+03:00"));
        appointmentToCreate.setEndTime(OffsetDateTime.parse("2025-02-11T11:00:00+03:00"));

        // test unknown schedule
        Mockito.when(appointmentsScheduleRepository.findById(Mockito.any(), Mockito.eq(LockModeType.PESSIMISTIC_WRITE))).thenReturn(null);
        assertThrows(EntityNotFoundByIdException.class, () -> appointmentScheduleService.createAppointment(scheduleId, appointmentToCreate));

        Mockito.when(appointmentsScheduleRepository.findById(scheduleId, LockModeType.PESSIMISTIC_WRITE)).thenReturn(scheduleEntity);

        // test end before start
        appointmentToCreate.setStartTime(OffsetDateTime.parse("2025-02-11T11:00:00+03:00"));
        appointmentToCreate.setEndTime(OffsetDateTime.parse("2025-02-11T10:00:00+03:00"));
        assertThrows(ValidationException.class, () -> appointmentScheduleService.createAppointment(scheduleId, appointmentToCreate));

        // test start minutes not zero
        appointmentToCreate.setStartTime(OffsetDateTime.parse("2025-02-11T10:01:00+03:00"));
        appointmentToCreate.setEndTime(OffsetDateTime.parse("2025-02-11T11:00:00+03:00"));
        assertThrows(ValidationException.class, () -> appointmentScheduleService.createAppointment(scheduleId, appointmentToCreate));

        // test end minutes not zero
        appointmentToCreate.setStartTime(OffsetDateTime.parse("2025-02-11T10:00:00+03:00"));
        appointmentToCreate.setEndTime(OffsetDateTime.parse("2025-02-11T11:01:00+03:00"));
        assertThrows(ValidationException.class, () -> appointmentScheduleService.createAppointment(scheduleId, appointmentToCreate));

        // test start seconds not zero
        appointmentToCreate.setStartTime(OffsetDateTime.parse("2025-02-11T10:00:05+03:00"));
        appointmentToCreate.setEndTime(OffsetDateTime.parse("2025-02-11T11:00:00+03:00"));
        assertThrows(ValidationException.class, () -> appointmentScheduleService.createAppointment(scheduleId, appointmentToCreate));

        // test end seconds not zero
        appointmentToCreate.setStartTime(OffsetDateTime.parse("2025-02-11T10:00:00+03:00"));
        appointmentToCreate.setEndTime(OffsetDateTime.parse("2025-02-11T11:00:05+03:00"));
        assertThrows(ValidationException.class, () -> appointmentScheduleService.createAppointment(scheduleId, appointmentToCreate));

        // test not 1 hour long
        appointmentToCreate.setStartTime(OffsetDateTime.parse("2025-02-11T10:00:00+03:00"));
        appointmentToCreate.setEndTime(OffsetDateTime.parse("2025-02-11T12:00:00+03:00"));
        assertThrows(ValidationException.class, () -> appointmentScheduleService.createAppointment(scheduleId, appointmentToCreate));

        // test start outside of schedule
        appointmentToCreate.setStartTime(OffsetDateTime.parse("2025-02-10T09:00:00+03:00"));
        appointmentToCreate.setEndTime(OffsetDateTime.parse("2025-02-10T10:00:00+03:00"));
        assertThrows(ValidationException.class, () -> appointmentScheduleService.createAppointment(scheduleId, appointmentToCreate));

        // test end outside of schedule
        appointmentToCreate.setStartTime(OffsetDateTime.parse("2025-02-11T16:00:00+03:00"));
        appointmentToCreate.setEndTime(OffsetDateTime.parse("2025-02-11T17:00:00+03:00"));
        assertThrows(ValidationException.class, () -> appointmentScheduleService.createAppointment(scheduleId, appointmentToCreate));

        // test intersects with existing
        scheduleEntity.setAppointments(new HashSet<>());
        AppointmentEntity appointmentEntity = new AppointmentEntity();
        appointmentEntity.setStartTime(OffsetDateTime.parse("2025-02-11T11:00:00+03:00"));
        appointmentEntity.setEndTime(OffsetDateTime.parse("2025-02-11T12:00:00+03:00"));
        scheduleEntity.getAppointments().add(appointmentEntity);

        appointmentToCreate.setStartTime(OffsetDateTime.parse("2025-02-11T11:00:00+03:00"));
        appointmentToCreate.setEndTime(OffsetDateTime.parse("2025-02-11T12:00:00+03:00"));
        assertThrows(ValidationException.class, () -> appointmentScheduleService.createAppointment(scheduleId, appointmentToCreate));
    }

    @Test
    @TestSecurity(user = TEST_KILLER_NAME, roles = {ApiConstant.ROLE_KILLER})
    void testDeleteAppointmentValidations() {
        UUID appointmentId = UUID.randomUUID();
        UUID scheduleId = UUID.randomUUID();

        AppointmentEntity appointmentEntity = new AppointmentEntity();
        appointmentEntity.setId(appointmentId);
        appointmentEntity.setStartTime(OffsetDateTime.parse("2024-11-11T10:00:00+03:00"));
        appointmentEntity.setEndTime(OffsetDateTime.parse("2024-11-11T11:00:00+03:00"));
        appointmentEntity.setBookedBy(testKillerEntity);

        Mockito.when(appointmentRepository.findById(appointmentId)).thenReturn(appointmentEntity);

        // test appointment already started
        assertThrows(ValidationException.class, () -> appointmentScheduleService.deleteAppointment(scheduleId, appointmentId));
    }
}