package ru.ifmo.se.johnwick.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.util.Collection;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "notification")
public class NotificationEntity extends BasicEntity {
    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "created_timestamp", nullable = false, insertable = false, updatable = false)
    private ZonedDateTime createdTimestamp;

    @ManyToOne
    @JoinColumn(name = "target_id")
    private UserEntity target;

    public NotificationEntity(String title, UserEntity target) {
        this.title = title;
        this.target = target;
    }

    public static Collection<NotificationEntity> findByTargetSortedByCreatedDesc(UserEntity target) {
        return find("target = ?1 order by createdTimestamp desc", target).list();
    }
}