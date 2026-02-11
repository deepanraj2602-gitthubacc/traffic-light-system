
package com.traffic.system.trafficlightservice.entity;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.traffic.system.trafficlightservice.enums.IntersectionAutoRunStatus;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "intersection")
@Getter
@Setter
@Builder
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class Intersection extends BaseEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(nullable = false, unique = true, updatable = false)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String idName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private IntersectionAutoRunStatus autoRunStatus;

    @OneToMany(mappedBy = "intersection", fetch = FetchType.EAGER, orphanRemoval = true, cascade = CascadeType.ALL)
    @JsonManagedReference("intersectionTrafficLightRef")
    private List<TrafficLight> trafficLights;

    public Intersection(String idName, IntersectionAutoRunStatus autoRunStatus, List<TrafficLight> trafficLights) {
        this.idName = idName;
        this.autoRunStatus = autoRunStatus;
        this.trafficLights = trafficLights;
    }
}
