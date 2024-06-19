package ru.ifmo.se.johnwick.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.ifmo.se.johnwick.entity.NotificationEntity;
import ru.ifmo.se.johnwick.model.dto.NotificationDto;

import java.util.Collection;

@Mapper(componentModel = MappingConstants.ComponentModel.JAKARTA,
        uses = {UserMapper.class})
public interface NotificationMapper {
    NotificationDto entityToDto(NotificationEntity entity);

    Collection<NotificationDto> entitiesToDtos(Collection<NotificationEntity> entities);
}
