package ru.ifmo.se.johnwick.rest;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.SecurityContext;
import ru.ifmo.se.johnwick.constant.ApiConstant;
import ru.ifmo.se.johnwick.model.Role;
import ru.ifmo.se.johnwick.model.dto.UserDto;
import ru.ifmo.se.johnwick.model.input.PasswordInput;
import ru.ifmo.se.johnwick.model.input.UserInput;
import ru.ifmo.se.johnwick.service.UserService;

import java.util.Collection;

@Path(ApiConstant.API_V1 + "/user")
@RolesAllowed("ADMIN")
public class UserController {
    @Inject
    UserService userService;

    @GET
    public Collection<UserDto> getUsers() {
        return userService.getAllUsers();
    }

    @GET
    @Path("/{role}")
    @RolesAllowed("KILLER")
    public Collection<UserDto> getUsersByRole(@PathParam("role") Role role) {
        return userService.getAllByRoles(role);
    }

    @POST
    @Transactional
    public UserDto createUser(UserInput userInput) {
        return userService.createUser(userInput);
    }

    @PUT
    @Transactional
    @Path("/{username}/password")
    public UserDto changeUserPassword(@PathParam("username") String username, PasswordInput passwordInput) {
        return userService.changeUserPassword(username, passwordInput);
    }

    @GET
    @RolesAllowed("**")
    @Path("/me")
    public UserDto getCurrentUser(@Context SecurityContext sec) {
        return userService.getUser(sec.getUserPrincipal().getName());
    }
}
