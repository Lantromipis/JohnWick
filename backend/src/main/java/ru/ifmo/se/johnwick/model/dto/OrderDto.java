package ru.ifmo.se.johnwick.model.dto;

import lombok.Data;
import ru.ifmo.se.johnwick.model.OrderType;

import java.time.Instant;

@Data
public class OrderDto {
    private OrderType type;
    private String description;
    private String status;
    private String targetName;
    private Instant createdTimestamp;
}
