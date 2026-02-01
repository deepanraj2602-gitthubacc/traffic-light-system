
package com.traffic.system.trafficlightservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrafficLightDTO extends BaseDTO {

    private String direction;
    private String color;
    private LocalDateTime lastChangedAt;
}
