package ru.ifmo.se.johnwick.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Order {
    private String type;
    private UserDto assignee;
    private UserDto debtor;
    private String customer;
    private Long price;
    private String target;
    private String description;
}
