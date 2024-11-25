package ru.ifmo.se.johnwick.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Set;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true, exclude = "applications")
public class RegularOrderDto extends OrderDto {
    private UserDto assignee;
    private long price;
    private String customerName;
    private Set<RegularOrderApplicationDto> applications;
}
