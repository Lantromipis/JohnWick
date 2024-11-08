package ru.ifmo.se.johnwick.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "\"promissory_note_order\"")
public class PromissoryNoteOrderEntity extends PanacheEntityBase {
    @Id
    @Column(name = "order_id", nullable = false)
    private UUID orderId = UUID.randomUUID();

    @ManyToOne
    @JoinColumn(name = "beneficiary_user_id", nullable = false)
    private UserEntity beneficiary;

    @ManyToOne
    @JoinColumn(name = "deptor_user_id", nullable = false)
    private UserEntity deptor;

}
