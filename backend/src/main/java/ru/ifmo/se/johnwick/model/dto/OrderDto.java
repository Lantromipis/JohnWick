package ru.ifmo.se.johnwick.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import ru.ifmo.se.johnwick.model.OrderType;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderDto {
    private long id;
    private OrderType type;
    private UserDto assignee;
    private UserDto beneficiary;
    private String customer;
    private Long price;
    private String target;
    private String description;
    private String canceled;
}
