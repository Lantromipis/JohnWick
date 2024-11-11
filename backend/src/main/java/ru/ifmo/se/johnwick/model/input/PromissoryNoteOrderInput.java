package ru.ifmo.se.johnwick.model.input;

import lombok.Data;
import ru.ifmo.se.johnwick.model.dto.UserDto;

@Data
public class PromissoryNoteOrderInput {
    private UserDto beneficiar;
    private UserDto deptor;
}
