package ru.ifmo.se.johnwick.rest.controller;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.SecurityContext;
import ru.ifmo.se.johnwick.constant.ApiConstant;
import ru.ifmo.se.johnwick.model.dto.UserDto;
import ru.ifmo.se.johnwick.service.api.UserService;

import java.util.List;
import java.util.UUID;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path(ApiConstant.API_V1 + "/user")
public class UserResource {

    @Inject
    UserService userService;

    @GET
    @RolesAllowed(ApiConstant.ROLE_ALL)
    public List<UserDto> listUsers(@QueryParam("rsqlPredicate") String rsqlPredicate) {
        return userService.listUsers(rsqlPredicate);
    }

    @GET
    @Path("/current")
    @RolesAllowed(ApiConstant.ROLE_ALL)
    public UserDto getCurrentUser(@Context SecurityContext securityContext) {
        String username = securityContext.getUserPrincipal().getName();
        return userService.getUserByUsername(username);
    }

    @POST
    @RolesAllowed(ApiConstant.ROLE_ADMIN)
    public UserDto createUser(UserDto userDto) {
        return userService.createUser(userDto);
    }

    @Path("/{id}")
    @PATCH
    @RolesAllowed(ApiConstant.ROLE_ADMIN)
    public UserDto patchUser(@PathParam("id") UUID userId, UserDto userDto) {
        userDto.setId(userId);
        return userService.updateUser(userDto);
    }
}
