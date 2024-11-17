package ru.ifmo.se.johnwick.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegularOrderApplicationDto {
    private UUID id;
    private UserDto killer;
    private OffsetDateTime createdTimestamp;
    private RegularOrderDto regularOrder;
}
