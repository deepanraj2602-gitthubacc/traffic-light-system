
package com.traffic.system.trafficlightservice.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record LightHistoryPageDTO(
        Integer pageNumber,
        Integer pageSize,
        Integer totalPages,
        Long totalElements,
        List<LightHistoryDTO> lightHistories
) {
}
