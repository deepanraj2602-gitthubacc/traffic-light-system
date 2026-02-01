
package com.traffic.system.trafficlightservice.service;


import com.traffic.system.trafficlightservice.dto.IntersectionDTO;
import com.traffic.system.trafficlightservice.dto.LightHistoryPageDTO;
import com.traffic.system.trafficlightservice.enums.Direction;
import com.traffic.system.trafficlightservice.enums.IntersectionAutoRunStatus;
import com.traffic.system.trafficlightservice.enums.LightColor;
import com.traffic.system.trafficlightservice.exception.TrafficLightServiceException;

public interface TrafficLightService {

    IntersectionDTO createIntersection(String idName) throws TrafficLightServiceException;

    IntersectionDTO changeLight(String idName, Direction direction, LightColor color) throws TrafficLightServiceException;

    IntersectionDTO updateIntersectionState(String idName, IntersectionAutoRunStatus autoRunStatus) throws TrafficLightServiceException;

    IntersectionDTO getIntersectionState(String idName) throws TrafficLightServiceException;

    LightHistoryPageDTO getLightHistory(String idName, Integer pageNumber, Integer pageSize) throws TrafficLightServiceException;
}
