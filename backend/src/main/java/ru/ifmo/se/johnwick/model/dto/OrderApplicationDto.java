package ru.ifmo.se.johnwick.model.dto;

import lombok.Data;

@Data
public class OrderApplicationDto {
    private UserDto appliedKiller;
    private OrderDto order;
}
