package ru.ifmo.se.johnwick.model.dto;

import lombok.Data;

@Data
public class RegularOrderDto {
    private Double price;
    private String customerName;
    private UserDto assignee;
}
