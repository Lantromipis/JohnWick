package ru.ifmo.se.johnwick.model.input;

import lombok.Data;
import ru.ifmo.se.johnwick.entity.UserEntity;
import ru.ifmo.se.johnwick.model.dto.UserDto;

@Data
public class HeadHuntOrderInput {
    private UserDto successer;
    private Double currentPrice;
    private String customerName;
}
