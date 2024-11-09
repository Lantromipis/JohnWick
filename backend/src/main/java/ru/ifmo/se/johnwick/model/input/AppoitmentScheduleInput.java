package ru.ifmo.se.johnwick.model.input;

import lombok.Data;
import ru.ifmo.se.johnwick.model.dto.UserDto;

import java.time.LocalDate;
import java.time.ZonedDateTime;

@Data
public class AppoitmentScheduleInput {
    private LocalDate date;
    private ZonedDateTime startTime;
    private ZonedDateTime endTime;
}
