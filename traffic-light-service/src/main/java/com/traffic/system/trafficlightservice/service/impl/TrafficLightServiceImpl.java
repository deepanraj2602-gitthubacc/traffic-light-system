
package com.traffic.system.trafficlightservice.service.impl;


import com.traffic.system.trafficlightservice.dto.IntersectionDTO;
import com.traffic.system.trafficlightservice.dto.LightHistoryPageDTO;
import com.traffic.system.trafficlightservice.enums.Direction;
import com.traffic.system.trafficlightservice.enums.IntersectionAutoRunStatus;
import com.traffic.system.trafficlightservice.enums.LightColor;
import com.traffic.system.trafficlightservice.exception.TrafficLightServiceException;
import com.traffic.system.trafficlightservice.service.TrafficLightService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.XSlf4j;
import org.springframework.stereotype.Service;

@XSlf4j
@Service
@RequiredArgsConstructor
public class TrafficLightServiceImpl implements TrafficLightService {

    public IntersectionDTO createIntersection(String idName) throws TrafficLightServiceException {
        return null;
    }

    public IntersectionDTO changeLight(String idName, Direction direction, LightColor color) throws TrafficLightServiceException {
        return null;
    }

    public IntersectionDTO updateIntersectionState(String idName, IntersectionAutoRunStatus autoRunStatus) throws TrafficLightServiceException {
        return null;
    }

    public IntersectionDTO getIntersectionState(String idName) throws TrafficLightServiceException {
        return null;
    }

    public LightHistoryPageDTO getLightHistory(String idName, Integer pageNumber, Integer pageSize) throws TrafficLightServiceException {
        return null;
    }
}
