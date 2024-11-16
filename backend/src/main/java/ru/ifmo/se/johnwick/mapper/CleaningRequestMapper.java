package ru.ifmo.se.johnwick.mapper;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Mappings;
import ru.ifmo.se.johnwick.entity.CleaningRequestEntity;
import ru.ifmo.se.johnwick.entity.UserEntity;
import ru.ifmo.se.johnwick.model.dto.CleaningRequestDto;
import ru.ifmo.se.johnwick.model.dto.UserDto;
import ru.ifmo.se.johnwick.model.input.CleaningRequestInput;
import ru.ifmo.se.johnwick.model.input.UserInput;

import java.util.Collection;

@Mapper(componentModel = MappingConstants.ComponentModel.JAKARTA)
public interface CleaningRequestMapper {

    CleaningRequestDto mapEntityToDto(CleaningRequestEntity entity);

    Collection<CleaningRequestDto> mapEntitiesToDtos(Collection<CleaningRequestEntity> entityCollection);

    @Mappings({
            @Mapping(expression = "java( ru.ifmo.se.johnwick.model.CleaningRequestStatus.valueOf(cleaningRequestInput.getStatus()) )", target = "status"),

    })
    CleaningRequestEntity mapInputToEntity(CleaningRequestInput cleaningRequestInput);
}
