package ru.ifmo.se.johnwick.rest.exceptionmapper;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import lombok.extern.slf4j.Slf4j;
import ru.ifmo.se.johnwick.model.dto.ErrorResponseDto;

import java.time.OffsetDateTime;

@Slf4j
@Provider
public class ThrowableExceptionMapper implements ExceptionMapper<Throwable> {
    @Override
    public Response toResponse(Throwable exception) {
        log.error("Unexpected error occurred.", exception);

        return Response
                .status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(
                        ErrorResponseDto
                                .builder()
                                .code(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode())
                                .message(exception.getMessage())
                                .timestamp(OffsetDateTime.now())
                                .build()
                )
                .build();
    }
}
