
package com.traffic.system.trafficlightservice.entity;


import com.traffic.system.trafficlightservice.enums.Direction;
import com.traffic.system.trafficlightservice.enums.IntersectionAutoRunStatus;
import com.traffic.system.trafficlightservice.enums.LightColor;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "light_history")
@Getter
@Setter
@Builder
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class LightHistory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(nullable = false, unique = true, updatable = false)
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Direction direction;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LightColor color;

    @Column(nullable = false)
    private LocalDateTime changedAt;

    @Column(nullable = false)
    private String intersectionIdName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private IntersectionAutoRunStatus intersectionAutoRunStatus;

    public LightHistory(Direction direction, LightColor color, LocalDateTime changedAt, String intersectionIdName, IntersectionAutoRunStatus intersectionAutoRunStatus) {
        this.direction = direction;
        this.color = color;
        this.changedAt = changedAt;
        this.intersectionIdName = intersectionIdName;
        this.intersectionAutoRunStatus = intersectionAutoRunStatus;
    }
}
