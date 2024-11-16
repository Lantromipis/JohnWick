package ru.ifmo.se.johnwick.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.ifmo.se.johnwick.model.entity.RegularOrderEntity;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegularOrderApplicationDto {
    private UUID id;
    private UserDto killer;
    private RegularOrderEntity regularOrder;
    private OffsetDateTime createdTimestamp;
}
