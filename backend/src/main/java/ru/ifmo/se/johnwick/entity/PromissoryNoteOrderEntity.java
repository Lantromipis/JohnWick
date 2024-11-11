package ru.ifmo.se.johnwick.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "\"promissory_note_order\"")
public class PromissoryNoteOrderEntity {
    @Id
    @Column(name = "order_id", nullable = false)
    private UUID orderId = UUID.randomUUID();

    @ManyToOne
    @JoinColumn(name = "beneficiary_user_id")
    private UserEntity beneficiary;

    @ManyToOne
    @JoinColumn(name = "deptor_user_id")
    private UserEntity deptor;

}
