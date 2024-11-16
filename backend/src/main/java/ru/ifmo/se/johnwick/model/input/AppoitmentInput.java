package ru.ifmo.se.johnwick.model.input;

import lombok.Data;
import ru.ifmo.se.johnwick.entity.UserEntity;
import ru.ifmo.se.johnwick.model.dto.UserDto;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class AppoitmentInput {
    private UserDto booker;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private String message;
}
