
package com.traffic.system.trafficlightservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class IntersectionDTO extends BaseDTO {

    private final String idName;
    private final String autoRunStatus;
    private List<TrafficLightDTO> trafficLights;
}
