package ru.ifmo.se.johnwick.model.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.OffsetTime;
import java.util.Set;
import java.util.UUID;

@Data
@Entity
@Table(name = "\"appointment_schedule\"")
public class AppointmentScheduleEntity {
    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "host_user_id", nullable = false)
    private UserEntity host;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "from_time")
    private OffsetTime startTime;

    @Column(name = "to_time")
    private OffsetTime endTime;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "appointments_schedule_id")
    private Set<AppointmentEntity> appointments;
}
