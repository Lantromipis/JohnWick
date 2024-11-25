package ru.ifmo.se.johnwick.service.api;

import ru.ifmo.se.johnwick.model.dto.AppointmentDto;
import ru.ifmo.se.johnwick.model.dto.AppointmentScheduleDto;

import java.util.List;
import java.util.UUID;

public interface AppointmentScheduleService {
    AppointmentScheduleDto createAppointmentSchedule(AppointmentScheduleDto appointmentScheduleDto);

    List<AppointmentScheduleDto> listAppointmentSchedule(String rsqlPredicate);

    List<AppointmentDto> listAppointments(String rsqlPredicate);

    AppointmentDto createAppointment(UUID appointmentScheduleId, AppointmentDto appointmentDto);

    void deleteAppointment(UUID appointmentScheduleId, UUID appointmentId);
}
