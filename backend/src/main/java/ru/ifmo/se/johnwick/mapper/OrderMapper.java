package ru.ifmo.se.johnwick.mapper;

import org.mapstruct.*;
import ru.ifmo.se.johnwick.model.dto.HeadHuntOrderDto;
import ru.ifmo.se.johnwick.model.dto.PromissoryNoteOrderDto;
import ru.ifmo.se.johnwick.model.dto.RegularOrderApplicationDto;
import ru.ifmo.se.johnwick.model.dto.RegularOrderDto;
import ru.ifmo.se.johnwick.model.entity.HeadHuntOrderEntity;
import ru.ifmo.se.johnwick.model.entity.PromissoryNoteOrderEntity;
import ru.ifmo.se.johnwick.model.entity.RegularOrderApplicationEntity;
import ru.ifmo.se.johnwick.model.entity.RegularOrderEntity;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.JAKARTA_CDI, uses = {UserMapper.class})
public abstract class OrderMapper {
    public abstract RegularOrderEntity mapRegularToEntity(RegularOrderDto regularOrderDto);

    @Mapping(target = "applications", qualifiedByName = "noOrder")
    public abstract RegularOrderDto mapRegularToDto(RegularOrderEntity regularOrderEntity);

    @Named("noApplications")
    @Mapping(target = "applications", ignore = true)
    public abstract RegularOrderDto mapRegularToDtoWithoutApplications(RegularOrderEntity regularOrderEntity);

    @IterableMapping(qualifiedByName = "noApplications")
    public abstract List<RegularOrderDto> mapRegularToDtoWithoutApplications(List<RegularOrderEntity> regularOrderEntity);

    @Mapping(target = "regularOrder", qualifiedByName = "noApplications")
    public abstract RegularOrderApplicationDto mapRegularOrderApplicationToDto(RegularOrderApplicationEntity regularOrderApplicationEntity);

    public abstract List<RegularOrderApplicationDto> mapRegularOrderApplicationToDto(List<RegularOrderApplicationEntity> regularOrderApplicationEntity);

    @Named("noOrder")
    @Mapping(target = "regularOrder", ignore = true)
    public abstract RegularOrderApplicationDto mapRegularOrderApplicationToDtoWithoutOrder(RegularOrderApplicationEntity regularOrderApplicationEntity);

    public abstract List<RegularOrderApplicationDto> mapRegularOrderApplicationToDtoWithoutOrder(List<RegularOrderApplicationEntity> regularOrderApplicationEntity);

    public abstract HeadHuntOrderEntity mapHeadHuntToEntity(HeadHuntOrderDto headHuntOrderDto);

    public abstract HeadHuntOrderDto mapHeadHuntToDto(HeadHuntOrderEntity headHuntOrderEntity);

    public abstract List<HeadHuntOrderDto> mapHeadHuntToDto(List<HeadHuntOrderEntity> headHuntOrderEntity);

    public abstract PromissoryNoteOrderEntity mapPromissoryNoteToEntity(PromissoryNoteOrderDto promissoryNoteOrderDto);

    public abstract PromissoryNoteOrderDto mapPromissoryNoteToDto(PromissoryNoteOrderEntity promissoryNoteOrderEntity);

    public abstract List<PromissoryNoteOrderDto> mapPromissoryNoteToDto(List<PromissoryNoteOrderEntity> promissoryNoteOrderEntity);
}
