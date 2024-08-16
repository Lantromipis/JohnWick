package ru.ifmo.se.johnwick.rest;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.SecurityContext;
import ru.ifmo.se.johnwick.constant.ApiConstant;
import ru.ifmo.se.johnwick.entity.UserEntity;
import ru.ifmo.se.johnwick.mapper.NotificationMapper;
import ru.ifmo.se.johnwick.model.dto.NotificationDto;
import ru.ifmo.se.johnwick.repository.NotificationRepository;
import ru.ifmo.se.johnwick.repository.UserRepository;

import java.util.Collection;

@Path(ApiConstant.API_V1 + "/notification")
public class NotificationResource {
    @Inject
    NotificationMapper notificationMapper;

    @Inject
    NotificationRepository notificationRepository;

    @Inject
    UserRepository userRepository;

    @GET
    public Collection<NotificationDto> getMyNotifications(@Context SecurityContext sec) {
        String username = sec.getUserPrincipal().getName();
        UserEntity userEntity = userRepository.findByUsername(username);

        return notificationMapper.entitiesToDtos(
                notificationRepository.findByTargetSortedByCreatedDesc(userEntity));
    }
}
