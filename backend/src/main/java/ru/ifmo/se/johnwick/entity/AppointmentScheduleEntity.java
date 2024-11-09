package ru.ifmo.se.johnwick.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Time;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.TimeZone;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "\"appointment_schedule\"")
public class AppointmentScheduleEntity extends PanacheEntityBase {

    @Id
    @Column(name = "id", nullable = false)
    private UUID id = UUID.randomUUID();

    @ManyToOne
    @JoinColumn(name = "host_user_id", nullable = false)
    private UserEntity hoster;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "from_time")
    private ZonedDateTime startTime;

    @Column(name = "to_time")
    private ZonedDateTime endTime;
}
