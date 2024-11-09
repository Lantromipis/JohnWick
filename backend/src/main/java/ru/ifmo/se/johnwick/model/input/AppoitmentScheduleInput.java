package ru.ifmo.se.johnwick.model.input;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import ru.ifmo.se.johnwick.model.dto.UserDto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.UUID;

@Data
@Getter
@Setter
public class AppoitmentScheduleInput {
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private UserDto hoster;
}
