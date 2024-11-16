package ru.ifmo.se.johnwick.model.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class AppoitmentDto {
    private UserDto booker;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private String message;
}
