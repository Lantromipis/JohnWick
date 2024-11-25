package ru.ifmo.se.johnwick.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.ifmo.se.johnwick.model.CleaningRequestStatus;
import ru.ifmo.se.johnwick.model.entity.OrderEntity;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CleaningRequestDto {
    private UUID id;
    private OrderEntity order;
    private UserDto requestedBy;
    private OffsetDateTime createdTimestamp;
    private CleaningRequestStatus status;
    private String details;
    private UserDto appliedCleaner;
}
