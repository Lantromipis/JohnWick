package ru.ifmo.se.johnwick.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Mappings;
import ru.ifmo.se.johnwick.entity.UserEntity;
import ru.ifmo.se.johnwick.model.dto.UserDto;
import ru.ifmo.se.johnwick.model.input.UserInput;

import java.util.Collection;

@Mapper(componentModel = MappingConstants.ComponentModel.JAKARTA)
public interface UserMapper {
    UserDto mapEntityToDto(UserEntity entity);

    Collection<UserDto> mapEntitiesToDtos(Collection<UserEntity> entityCollection);

    @Mappings({
            @Mapping(expression = "java( ru.ifmo.se.johnwick.model.Role.valueOf(userInput.getRole()) )", target = "role"),
            @Mapping(expression = "java( io.quarkus.elytron.security.common.BcryptUtil.bcryptHash(userInput.getPassword()) )", target = "password")
    })
    UserEntity mapInputToEntity(UserInput userInput);
}
