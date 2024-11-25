package ru.ifmo.se.johnwick.model.dto;

import lombok.*;

import java.time.OffsetDateTime;
import java.util.Set;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = "appointments")
public class AppointmentScheduleDto {
    private UUID id;
    private UserDto host;
    private OffsetDateTime startTime;
    private OffsetDateTime endTime;
    private Set<AppointmentDto> appointments;
}
