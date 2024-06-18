package ru.ifmo.se.johnwick.model.input;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;
import ru.ifmo.se.johnwick.model.OrderType;

@Data
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "type",
        visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = RegularOrderInput.class, name = "REGULAR"),
        @JsonSubTypes.Type(value = HeadHuntOrderInput.class, name = "HEAD_HUNT"),
        @JsonSubTypes.Type(value = PromissoryNoteOrderInput.class, name = "PROMISSORY_NOTE")
})
public abstract class OrderInput {
    private OrderType type;
    private UsernameInput assignee;
    private String target;
    private String customer;
    private Long price;
    private String description;
}
