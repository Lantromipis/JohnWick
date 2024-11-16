package ru.ifmo.se.johnwick.model.dto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import ru.ifmo.se.johnwick.model.OrderStatus;
import ru.ifmo.se.johnwick.model.OrderType;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = RegularOrderDto.class, name = "REGULAR"),
        @JsonSubTypes.Type(value = HeadHuntOrderDto.class, name = "HEAD_HUNT"),
        @JsonSubTypes.Type(value = PromissoryNoteOrderDto.class, name = "PROMISSORY_NOTE")
})
public abstract class OrderDto {
    private UUID id;
    private OffsetDateTime createdTimestamp;
    private OrderType type;
    private String description;
    private OrderStatus status;
    private String targetName;
}
