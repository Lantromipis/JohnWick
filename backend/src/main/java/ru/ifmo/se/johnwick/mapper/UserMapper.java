package ru.ifmo.se.johnwick.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import ru.ifmo.se.johnwick.model.dto.UserDto;
import ru.ifmo.se.johnwick.model.entity.UserEntity;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.CDI)
public abstract class UserMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(expression = "java( io.quarkus.elytron.security.common.BcryptUtil.bcryptHash(dto.getPassword()) )", target = "password")
    public abstract UserEntity fromDto(UserDto dto);

    @Mapping(target = "password", ignore = true)
    public abstract UserDto toDto(UserEntity entity);

    public abstract List<UserDto> toDto(List<UserEntity> entity);
}
