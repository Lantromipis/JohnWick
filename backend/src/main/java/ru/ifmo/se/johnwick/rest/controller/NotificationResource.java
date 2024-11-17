package ru.ifmo.se.johnwick.rest.controller;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import ru.ifmo.se.johnwick.constant.ApiConstant;
import ru.ifmo.se.johnwick.model.dto.NotificationDto;
import ru.ifmo.se.johnwick.service.api.NotificationService;

import java.util.List;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path(ApiConstant.API_V1 + "/notification")
public class NotificationResource {

    @Inject
    NotificationService notificationService;

    @GET
    public List<NotificationDto> listNotifications() {
        return notificationService.listCurrentUserNotifications();
    }
}
