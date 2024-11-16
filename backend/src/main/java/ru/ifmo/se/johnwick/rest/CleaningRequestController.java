package ru.ifmo.se.johnwick.rest;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import ru.ifmo.se.johnwick.constant.ApiConstant;
import ru.ifmo.se.johnwick.model.dto.AppointmentScheduleDto;
import ru.ifmo.se.johnwick.model.dto.CleaningRequestDto;
import ru.ifmo.se.johnwick.model.input.AppoitmentScheduleInput;
import ru.ifmo.se.johnwick.model.input.CleaningRequestInput;
import ru.ifmo.se.johnwick.service.AppoitmentScheduleService;
import ru.ifmo.se.johnwick.service.CleaningRequestService;


import java.util.Collection;

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
}
