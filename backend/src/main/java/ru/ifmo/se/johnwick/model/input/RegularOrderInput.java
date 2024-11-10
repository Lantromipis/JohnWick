package ru.ifmo.se.johnwick.model.input;

import lombok.Data;
import ru.ifmo.se.johnwick.model.dto.UserDto;

@Data
public class RegularOrderInput {
    private Double price;
    private String customerName;
    private UserDto assignee;
}
