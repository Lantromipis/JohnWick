package ru.ifmo.se.johnwick.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class TargetOrderInput extends OrderInput {
    private String target;
}
