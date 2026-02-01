
package com.traffic.system.trafficlightservice.controller;


import com.traffic.system.trafficlightservice.dto.IntersectionDTO;
import com.traffic.system.trafficlightservice.dto.LightHistoryPageDTO;
import com.traffic.system.trafficlightservice.exception.TrafficLightServiceException;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.XSlf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@XSlf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/v1/intersections/{id}")
@Tag(name = "Traffic Light Controller", description = "Traffic light operation APIs")
public class TrafficLightController {

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<IntersectionDTO> createIntersection(@NotBlank(message = "Intersection ID is mandatory")
                                                              @PathVariable String idName) throws TrafficLightServiceException {
        return null;
    }

    @PostMapping(path = "/changelight", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<IntersectionDTO> changeLight(@NotBlank(message = "Intersection ID is mandatory") @PathVariable String idName,
                                                       @NotBlank(message = "Direction is mandatory") @RequestParam String direction,
                                                       @NotBlank(message = "Signal Color is mandatory") @RequestParam String color)
            throws TrafficLightServiceException {
        return null;
    }

    @PostMapping(path = "/{state}light", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<IntersectionDTO> updateIntersectionState(@NotBlank(message = "Intersection ID is mandatory") @PathVariable String idName,
                                                                   @NotBlank(message = "State is mandatory") @PathVariable String state)
            throws TrafficLightServiceException {
        return null;
    }

    @GetMapping(path = "/lightstate", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<IntersectionDTO> getIntersectionState(@NotBlank(message = "Intersection ID is mandatory") @PathVariable String idName)
            throws TrafficLightServiceException {
        return null;
    }

    @GetMapping(path = "/lighthistory", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LightHistoryPageDTO> getLightHistory(@NotBlank(message = "Intersection ID is mandatory") @PathVariable String idName,
                                                               @NotBlank(message = "Page Number is mandatory") @RequestParam Integer pageNumber,
                                                               @NotBlank(message = "Page Size is mandatory") @RequestParam Integer pageSize)
            throws TrafficLightServiceException {
        return null;
    }
}
