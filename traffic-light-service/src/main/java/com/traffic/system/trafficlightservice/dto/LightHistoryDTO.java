
package com.traffic.system.trafficlightservice.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record LightHistoryDTO(
        String color,
        String direction,
        LocalDateTime changedAt,
        String intersectionIdName,
        String intersectionAutoRunStatus
) {
}
