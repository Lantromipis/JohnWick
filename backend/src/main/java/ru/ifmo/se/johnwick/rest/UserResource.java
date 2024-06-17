package ru.ifmo.se.johnwick.rest;

import io.quarkus.elytron.security.common.BcryptUtil;
import io.quarkus.panache.common.Page;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.SecurityContext;
import ru.ifmo.se.johnwick.entity.UserEntity;
import ru.ifmo.se.johnwick.mapper.UserMapper;
import ru.ifmo.se.johnwick.model.PasswordInput;
import ru.ifmo.se.johnwick.model.UserDto;
import ru.ifmo.se.johnwick.model.UserInput;

import java.util.Collection;

@Path("/user")
@RolesAllowed("ADMIN")
public class UserResource {
    @Inject
    UserMapper userMapper;

    @GET
    public Collection<UserDto> getUsers(@QueryParam("limit") @DefaultValue("0") int limit,
                                        @QueryParam("offset") @DefaultValue("10") int offset) {
        Collection<UserEntity> entityCollection = UserEntity.findByPage(Page.of(offset, limit));
        return userMapper.mapEntitiesToDtos(entityCollection);
    }

    @POST
    @Transactional
    public UserDto createUser(UserInput userInput) {
        UserEntity entity = userMapper.mapInputToEntity(userInput);
        entity.persist();
        return userMapper.mapEntityToDto(entity);
    }

    @PUT
    @Transactional
    @Path("/{username}/password")
    public UserDto changeUserPassword(@PathParam("username") String username, PasswordInput passwordInput) {
        UserEntity entity = UserEntity.findByUsername(username);
        entity.setPassword(BcryptUtil.bcryptHash(passwordInput.getPassword()));
        entity.persist();
        return userMapper.mapEntityToDto(entity);
    }

    @GET
    @RolesAllowed("**")
    @Path("/me")
    public UserDto getCurrentUser(@Context SecurityContext sec) {
        String username = sec.getUserPrincipal().getName();
        UserEntity userEntity = UserEntity.findByUsername(username);
        return userMapper.mapEntityToDto(userEntity);
    }
}
