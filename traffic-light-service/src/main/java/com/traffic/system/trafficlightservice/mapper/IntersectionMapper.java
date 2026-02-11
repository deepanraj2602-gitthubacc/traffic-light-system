
package com.traffic.system.trafficlightservice.mapper;


import com.traffic.system.trafficlightservice.dto.IntersectionDTO;
import com.traffic.system.trafficlightservice.entity.Intersection;
import com.traffic.system.trafficlightservice.enums.IntersectionAutoRunStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface IntersectionMapper {

    @Mapping(source = "autoRunStatus", target = "autoRunStatus", qualifiedByName = "mapDtoAutoRunStatus")
    @Mapping(source = "trafficLights", target = "trafficLights", ignore = true)
    IntersectionDTO toDto(Intersection intersection);

    @Mapping(source = "autoRunStatus", target = "autoRunStatus", qualifiedByName = "mapDtoAutoRunStatus")
    IntersectionDTO toDtoWithTrafficLights(Intersection intersection);

    @Named("mapDtoAutoRunStatus")
    default String mapDtoAutoRunStatus(IntersectionAutoRunStatus autoRunStatus) {
        return switch (autoRunStatus) {
            case PAUSED -> "PAUSED";
            default -> "RUNNING";
        };
    }
}
