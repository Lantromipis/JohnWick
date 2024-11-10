package ru.ifmo.se.johnwick.model.input;

import lombok.Data;
import ru.ifmo.se.johnwick.model.OrderType;


@Data
public class OrderInput {
    private OrderType type;
    private String description;
    private String status;
    private String targetName;

}
