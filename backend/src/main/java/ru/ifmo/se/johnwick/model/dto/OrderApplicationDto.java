package ru.ifmo.se.johnwick.model.dto;

import lombok.Data;

@Data
public class OrderApplicationDto {
    private long id;
    private UserDto appliedKiller;
    private OrderDto order;
}
