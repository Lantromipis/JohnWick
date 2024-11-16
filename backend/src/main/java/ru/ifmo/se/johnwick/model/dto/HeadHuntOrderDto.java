package ru.ifmo.se.johnwick.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class HeadHuntOrderDto extends OrderDto {
    private UserDto succeededKiller;
    private Double currentPrice;
    private String customerName;
}
