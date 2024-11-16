package ru.ifmo.se.johnwick.rest;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import ru.ifmo.se.johnwick.constant.ApiConstant;
import ru.ifmo.se.johnwick.model.dto.AppoitmentDto;
import ru.ifmo.se.johnwick.model.input.AppoitmentInput;
import ru.ifmo.se.johnwick.service.AppoitmentService;

@Path(ApiConstant.API_V1 + "/appoitment-schedule")
@RolesAllowed({"KILLER"})
public class AppoitmentController {

    @Inject
    AppoitmentService appoitmentService;

    @POST
    @Transactional
    public AppoitmentDto createAppointmentSchedule(AppoitmentInput appoitmentInput){
        return appoitmentService.createAppointmentSchedule(appoitmentInput);
    }
}
