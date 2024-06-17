package ru.ifmo.se.johnwick.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;

@Data
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "type",
        visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = TargetOrderInput.class, name = "DEFAULT"),
        @JsonSubTypes.Type(value = TargetOrderInput.class, name = "HEAD_HUNT"),
        @JsonSubTypes.Type(value = BillOrderInput.class, name = "BILL")
})
public abstract class OrderInput {
    private OrderType type;
    private String customer;
    private String price;
    private String description;
}
