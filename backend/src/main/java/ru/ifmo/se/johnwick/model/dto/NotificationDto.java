package ru.ifmo.se.johnwick.model.dto;

import lombok.Data;

import java.time.ZonedDateTime;

@Data
public class NotificationDto {
    private String title;
    private ZonedDateTime createdTimestamp;
    private UserDto target;
}
