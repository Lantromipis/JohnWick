package ru.ifmo.se.johnwick.model.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "\"appointment\"")
public class AppointmentEntity {
    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "booked_by_user_id", nullable = false)
    private UserEntity bookedBy;

    @Column(name = "from_time", nullable = false)
    private OffsetDateTime startTime;

    @Column(name = "to_time", nullable = false)
    private OffsetDateTime endTime;

    @Column(name = "message")
    private String message;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appointments_schedule_id")
    private AppointmentScheduleEntity appointmentSchedule;
}
