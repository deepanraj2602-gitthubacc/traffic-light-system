
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
public class LightHistoryDTO extends BaseDTO {

    private String direction;
    private String color;
    private LocalDateTime changedAt;
    private String intersectionIdName;
    private String intersectionAutoRunStatus;
}
