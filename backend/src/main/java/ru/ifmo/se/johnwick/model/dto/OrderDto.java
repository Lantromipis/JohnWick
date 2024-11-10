package ru.ifmo.se.johnwick.model.dto;

import lombok.Data;
import ru.ifmo.se.johnwick.model.OrderStatus;
import ru.ifmo.se.johnwick.model.OrderType;

import java.time.Instant;

@Data
public class OrderDto {
    private OrderType type;
    private OrderStatus status;
    private Instant createdTimestamp;
    private String description;
    private String targetName;
}


