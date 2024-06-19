package ru.ifmo.se.johnwick.model.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class AvailableOrderDto extends OrderDto {
    private boolean alreadyApplied;
}
