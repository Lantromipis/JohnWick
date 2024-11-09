package ru.ifmo.se.johnwick.model.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.UUID;

@Data
public class AppointmentScheduleDto {
    private LocalDate date;
    private UserDto hoster;
    private ZonedDateTime startTime;
    private ZonedDateTime endTime;
}
