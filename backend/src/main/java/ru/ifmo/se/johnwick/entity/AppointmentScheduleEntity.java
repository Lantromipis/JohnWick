package ru.ifmo.se.johnwick.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "\"appointment_schedule\"")
public class AppointmentScheduleEntity {

    @Id
    @Column(name = "id", nullable = false)
    private UUID id = UUID.randomUUID();

    @ManyToOne
    @JoinColumn(name = "host_user_id", nullable = false)
    private UserEntity hoster;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "from_time")
    private LocalTime startTime;

    @Column(name = "to_time")
    private LocalTime endTime;
}
