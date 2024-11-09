package ru.ifmo.se.johnwick.model.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.ZonedDateTime;

@Data
public class AppointmentDto {
    private LocalDate date;
    private UserDto booker;
    private ZonedDateTime startTime;
    private ZonedDateTime endTime;
    private String message;
}
