package ru.ifmo.se.johnwick.rest;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import ru.ifmo.se.johnwick.constant.ApiConstant;
import ru.ifmo.se.johnwick.entity.CleaningRequestEntity;
import ru.ifmo.se.johnwick.model.CleaningRequestStatus;
import ru.ifmo.se.johnwick.model.dto.CleaningRequestDto;
import ru.ifmo.se.johnwick.model.input.CleaningRequestInput;
import ru.ifmo.se.johnwick.repository.CleaningRequestRepository;
import ru.ifmo.se.johnwick.service.CleaningRequestService;


import java.util.Collection;
import java.util.UUID;

@Path(ApiConstant.API_V1 + "/cleaning")
public class CleaningRequestController {
    @Inject
    CleaningRequestService cleaningRequestService;

    @POST
    @RolesAllowed("KILLER")
    @Transactional
    public CleaningRequestDto createCleaningRequest(CleaningRequestInput cleaningRequestInput){
        return cleaningRequestService.createCleaningRequest(cleaningRequestInput);
    }

    @GET
    public Collection<CleaningRequestDto> getAppointmentScheduleByDateRange() {
        return cleaningRequestService.getAllCleaningRequest();
    }

    @PUT
    @Path("/{id}/accept")
    @Transactional
    public CleaningRequestDto acceptCleaningRequest(@PathParam("id") UUID requestId) {
        return  cleaningRequestService.acceptCleaningRequest(requestId);
    }

    @PUT
    @Path("/{id}/finish")
    @Transactional
    public CleaningRequestDto finishCleaningRequest(@PathParam("id") UUID requestId) {
        return cleaningRequestService.finishCleaningRequest(requestId);
    }
}
