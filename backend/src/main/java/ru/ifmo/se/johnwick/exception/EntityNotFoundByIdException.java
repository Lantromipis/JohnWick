package ru.ifmo.se.johnwick.exception;

public class EntityNotFoundByIdException extends RuntimeException {
    public EntityNotFoundByIdException(String entityType, String id) {
        super("Entity with type '" + entityType + "' and id '" + id + "' not found.");
    }
}
