package ru.ifmo.se.johnwick.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.ifmo.se.johnwick.model.dto.NotificationDto;
import ru.ifmo.se.johnwick.model.entity.NotificationEntity;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.JAKARTA_CDI, uses = {UserMapper.class})
public abstract class NotificationMapper {
    public abstract NotificationDto toDto(NotificationEntity notificationEntity);

    public abstract List<NotificationDto> toDto(List<NotificationEntity> notificationEntity);
}
