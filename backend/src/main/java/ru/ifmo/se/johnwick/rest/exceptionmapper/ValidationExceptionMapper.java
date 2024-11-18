package ru.ifmo.se.johnwick.rest.exceptionmapper;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import lombok.extern.slf4j.Slf4j;
import ru.ifmo.se.johnwick.exception.ValidationException;
import ru.ifmo.se.johnwick.model.dto.ErrorResponseDto;

import java.time.OffsetDateTime;

@Slf4j
@Provider
public class ValidationExceptionMapper implements ExceptionMapper<ValidationException> {
    @Override
    public Response toResponse(ValidationException exception) {
        log.error("Validation error.", exception);

        return Response
                .status(Response.Status.BAD_REQUEST)
                .entity(
                        ErrorResponseDto
                                .builder()
                                .code(Response.Status.BAD_REQUEST.getStatusCode())
                                .message(exception.getMessage())
                                .timestamp(OffsetDateTime.now())
                                .build()
                )
                .build();
    }
}
