package ru.ifmo.se.johnwick.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.OffsetTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentDto {
    private UUID id;
    private UserDto bookedBy;
    private LocalDate date;
    private OffsetTime startTime;
    private OffsetTime endTime;
    private String message;
}
