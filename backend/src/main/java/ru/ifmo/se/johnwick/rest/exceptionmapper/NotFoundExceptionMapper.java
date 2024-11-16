package ru.ifmo.se.johnwick.rest.exceptionmapper;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import lombok.extern.slf4j.Slf4j;
import ru.ifmo.se.johnwick.exception.EntityNotFoundByIdException;
import ru.ifmo.se.johnwick.model.dto.ErrorResponseDto;

import java.time.OffsetDateTime;

@Slf4j
@Provider
public class NotFoundExceptionMapper implements ExceptionMapper<EntityNotFoundByIdException> {
    @Override
    public Response toResponse(EntityNotFoundByIdException exception) {
        log.error("Entity not found.", exception);

        return Response
                .status(Response.Status.NOT_FOUND)
                .entity(
                        ErrorResponseDto
                                .builder()
                                .code(Response.Status.NOT_FOUND.getStatusCode())
                                .message(exception.getMessage())
                                .timestamp(OffsetDateTime.now())
                                .build()
                )
                .build();
    }
}
