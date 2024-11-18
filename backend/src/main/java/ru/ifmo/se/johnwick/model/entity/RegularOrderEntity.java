package ru.ifmo.se.johnwick.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Set;

@Data
@Entity
@Table(name = "\"regular_order\"")
@EqualsAndHashCode(callSuper = true, exclude = "applications")
@PrimaryKeyJoinColumn(name = "order_id")
public class RegularOrderEntity extends OrderEntity {
    @ManyToOne(fetch = FetchType.EAGER, optional = true)
    @JoinColumn(name = "assigned_killer_id", nullable = true)
    private UserEntity assignee;

    @Column(name = "price")
    private long price;

    @Column(name = "customer_name")
    private String customerName;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "regularOrder", orphanRemoval = true)
    private Set<RegularOrderApplicationEntity> applications;
}
