
package com.traffic.system.trafficlightservice.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.traffic.system.trafficlightservice.enums.Direction;
import com.traffic.system.trafficlightservice.enums.LightColor;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "traffic_light")
@Getter
@Setter
@Builder
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class TrafficLight extends BaseEntity {

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

    private LocalDateTime lastChangedAt;

    @ManyToOne
    @JsonBackReference("intersectionTrafficLightRef")
    @JoinColumn(name = "intersection_id", referencedColumnName = "id")
    private Intersection intersection;

    public TrafficLight(Direction direction, LightColor color, LocalDateTime lastChangedAt) {
        this.direction = direction;
        this.color = color;
        this.lastChangedAt = lastChangedAt;
    }
}
