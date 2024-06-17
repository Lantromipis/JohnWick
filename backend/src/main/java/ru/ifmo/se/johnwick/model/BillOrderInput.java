package ru.ifmo.se.johnwick.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class BillOrderInput extends OrderInput {
    private String debtorUsername;
}
