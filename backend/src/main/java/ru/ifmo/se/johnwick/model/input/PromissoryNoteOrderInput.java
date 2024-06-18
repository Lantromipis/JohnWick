package ru.ifmo.se.johnwick.model.input;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class PromissoryNoteOrderInput extends OrderInput {
    private UsernameInput beneficiary;
}
