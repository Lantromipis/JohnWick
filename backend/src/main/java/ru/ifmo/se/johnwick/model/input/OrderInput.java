package ru.ifmo.se.johnwick.model.input;

import lombok.Data;
import ru.ifmo.se.johnwick.model.OrderStatus;
import ru.ifmo.se.johnwick.model.OrderType;


@Data
public class OrderInput {
    private String type;
    private String status;
    private String description;
    private String targetName;
}
