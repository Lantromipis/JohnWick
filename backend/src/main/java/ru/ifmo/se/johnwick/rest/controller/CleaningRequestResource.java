package ru.ifmo.se.johnwick.rest.controller;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import ru.ifmo.se.johnwick.constant.ApiConstant;
import ru.ifmo.se.johnwick.model.dto.CleaningRequestDto;
import ru.ifmo.se.johnwick.service.api.CleaningRequestService;

import java.util.List;
import java.util.UUID;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path(ApiConstant.API_V1 + "/cleaningRequest")
public class CleaningRequestResource {

    @Inject
    CleaningRequestService cleaningRequestService;

    @GET
    @RolesAllowed(ApiConstant.ROLE_CLEANER)
    public List<CleaningRequestDto> listCleanings(@QueryParam("rsqlPredicate") String rsqlPredicate) {
        return cleaningRequestService.listCleanings(rsqlPredicate);
    }

    @PATCH
    @Path("/{id}")
    @RolesAllowed(ApiConstant.ROLE_CLEANER)
    public CleaningRequestDto listCleanings(@PathParam("id") UUID id, CleaningRequestDto cleaningRequestDto) {
        cleaningRequestDto.setId(id);
        return cleaningRequestService.updateCleaning(cleaningRequestDto);
    }
}
