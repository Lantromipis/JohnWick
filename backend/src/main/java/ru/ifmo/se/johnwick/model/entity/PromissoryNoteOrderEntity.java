package ru.ifmo.se.johnwick.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
@PrimaryKeyJoinColumn(name = "order_id")
@Table(name = "\"promissory_note_order\"")
public class PromissoryNoteOrderEntity extends OrderEntity {
    @ManyToOne
    @JoinColumn(name = "beneficiary_user_id")
    private UserEntity beneficiary;

    @ManyToOne
    @JoinColumn(name = "debtor_user_id")
    private UserEntity debtor;

}
