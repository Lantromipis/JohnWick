package ru.ifmo.se.johnwick.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.Set;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentScheduleDto {
    private UUID id;
    private UserDto host;
    private OffsetDateTime startTime;
    private OffsetDateTime endTime;
    private Set<AppointmentDto> appointments;
}
