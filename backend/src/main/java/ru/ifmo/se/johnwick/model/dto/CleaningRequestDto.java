package ru.ifmo.se.johnwick.model.dto;

import lombok.Data;
import ru.ifmo.se.johnwick.entity.UserEntity;
import ru.ifmo.se.johnwick.model.CleaningRequestStatus;

import java.time.Instant;

@Data
public class CleaningRequestDto {
    private UserDto requester;
    private Instant createdTimestamp;
    private Double price;
    private String status;
    private String details;
}
