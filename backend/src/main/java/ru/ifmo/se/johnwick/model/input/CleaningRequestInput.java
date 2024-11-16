package ru.ifmo.se.johnwick.model.input;

import lombok.Data;
import ru.ifmo.se.johnwick.model.CleaningRequestStatus;
import ru.ifmo.se.johnwick.model.dto.UserDto;

import java.time.Instant;

@Data
public class CleaningRequestInput {
    private UserDto requester;
    private Double price;
    private String details;
    private String status;
}
