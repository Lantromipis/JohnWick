package ru.ifmo.se.johnwick.rest.controller;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import ru.ifmo.se.johnwick.constant.ApiConstant;
import ru.ifmo.se.johnwick.model.dto.AppointmentDto;
import ru.ifmo.se.johnwick.model.dto.AppointmentScheduleDto;
import ru.ifmo.se.johnwick.service.api.AppointmentScheduleService;

import java.util.List;
import java.util.UUID;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path(ApiConstant.API_V1 + "/appointmentSchedule")
public class AppointmentScheduleResource {

    @Inject
    AppointmentScheduleService appointmentScheduleService;

    @GET
    @RolesAllowed({ApiConstant.ROLE_KILLER, ApiConstant.ROLE_SOMMELIER, ApiConstant.ROLE_ADMIN})
    public List<AppointmentScheduleDto> listAppointmentSchedule(@QueryParam("rsqlPredicate") String rsqlPredicate) {
        return appointmentScheduleService.listAppointmentSchedule(rsqlPredicate);
    }

    @POST
    @RolesAllowed({ApiConstant.ROLE_SOMMELIER, ApiConstant.ROLE_ADMIN})
    public AppointmentScheduleDto createAppointmentSchedule(AppointmentScheduleDto appointmentScheduleDto) {
        return appointmentScheduleService.createAppointmentSchedule(appointmentScheduleDto);
    }

    @POST
    @Path("/{id}/appointments")
    @RolesAllowed({ApiConstant.ROLE_KILLER})
    public AppointmentDto createAppointment(@PathParam("id") UUID appointmentScheduleId,
                                            AppointmentDto appointmentDto) {
        return appointmentScheduleService.createAppointment(appointmentScheduleId, appointmentDto);
    }
}
