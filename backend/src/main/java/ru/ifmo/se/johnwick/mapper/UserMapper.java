package ru.ifmo.se.johnwick.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import ru.ifmo.se.johnwick.entity.UserEntity;
import ru.ifmo.se.johnwick.model.UserDto;
import ru.ifmo.se.johnwick.model.UserInput;

import java.util.Collection;

@Mapper(componentModel = "cdi")
public interface UserMapper {
    UserDto mapEntityToDto(UserEntity entity);
    Collection<UserDto> mapEntitiesToDtos(Collection<UserEntity> entityCollection);
    @Mappings({
            @Mapping(expression = "java( ru.ifmo.se.johnwick.model.Role.byString(userInput.getRole()) )", target = "role"),
            @Mapping(expression = "java( io.quarkus.elytron.security.common.BcryptUtil.bcryptHash(userInput.getPassword()) )", target = "password")
    })
    UserEntity mapInputToEntity(UserInput userInput);
}
