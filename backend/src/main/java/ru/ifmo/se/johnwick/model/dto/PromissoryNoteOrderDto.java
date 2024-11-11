package ru.ifmo.se.johnwick.model.dto;

import lombok.Data;

@Data
public class PromissoryNoteOrderDto {
    private UserDto beneficiar;
    private UserDto deptor;
}
