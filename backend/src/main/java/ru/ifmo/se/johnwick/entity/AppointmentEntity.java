package ru.ifmo.se.johnwick.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.TimeZone;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "\"appointment\"")
public class AppointmentEntity extends PanacheEntityBase {
    @Id
    @Column(name = "id", nullable = false)
    private UUID id = UUID.randomUUID();

    @ManyToOne
    @JoinColumn(name = "booked_by_user_id", nullable = false)
    private UserEntity booker;

    @ManyToOne
    @JoinColumn(name = "appointment_schedule_id", nullable = false)
    private AppointmentScheduleEntity appointmentSchedule;

    @Column(name = "date", nullable = false, insertable = false, updatable = false)
    private LocalDate date;

    @Column(name = "from_time", nullable = false, insertable = false, updatable = false)
    private ZonedDateTime startTime;

    @Column(name = "to_time", nullable = false, insertable = false, updatable = false)
    private ZonedDateTime endTime;

    @Column(name = "message")
    private String message;
}
