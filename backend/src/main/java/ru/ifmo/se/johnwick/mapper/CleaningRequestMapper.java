package ru.ifmo.se.johnwick.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.ifmo.se.johnwick.model.dto.CleaningRequestDto;
import ru.ifmo.se.johnwick.model.entity.CleaningRequestEntity;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.JAKARTA_CDI, uses = {UserMapper.class})
public abstract class CleaningRequestMapper {
    public abstract CleaningRequestDto mapDtoFromEntity(CleaningRequestEntity entity);

    public abstract List<CleaningRequestDto> mapDtoFromEntity(List<CleaningRequestEntity> entity);
}
