package ru.ifmo.se.johnwick.rest;


import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import org.jboss.logging.annotations.Pos;
import ru.ifmo.se.johnwick.constant.ApiConstant;
import ru.ifmo.se.johnwick.model.Role;
import ru.ifmo.se.johnwick.model.dto.AppointmentScheduleDto;
import ru.ifmo.se.johnwick.model.dto.UserDto;
import ru.ifmo.se.johnwick.model.input.AppoitmentScheduleInput;
import ru.ifmo.se.johnwick.service.AppoitmentScheduleService;

import java.time.LocalDate;
import java.util.Collection;

@Path(ApiConstant.API_V1 + "/schedule")
@RolesAllowed({"TAILOR","SOMMELIER"})
public class AppoitmentScheduleController {
    @Inject
    AppoitmentScheduleService appoitmentScheduleService;

    @POST
    @Transactional
    public AppointmentScheduleDto createAppointmentSchedule(AppoitmentScheduleInput appoitmentScheduleInput){
        return appoitmentScheduleService.createAppointmentSchedule(appoitmentScheduleInput);
    }


    @GET
    public Collection<AppointmentScheduleDto> getAppointmentScheduleByDate(@QueryParam("date") String dateString) {
        LocalDate date = LocalDate.parse(dateString);
        return appoitmentScheduleService.getAppointmentScheduleByDate(date);
    }
}
