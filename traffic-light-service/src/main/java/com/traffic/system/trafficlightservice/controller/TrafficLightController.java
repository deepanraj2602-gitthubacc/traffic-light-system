
package com.traffic.system.trafficlightservice.controller;


import com.traffic.system.trafficlightservice.dto.IntersectionDTO;
import com.traffic.system.trafficlightservice.dto.LightHistoryPageDTO;
import com.traffic.system.trafficlightservice.enums.Direction;
import com.traffic.system.trafficlightservice.enums.IntersectionAutoRunStatus;
import com.traffic.system.trafficlightservice.enums.LightColor;
import com.traffic.system.trafficlightservice.exception.TrafficLightServiceException;
import com.traffic.system.trafficlightservice.service.TrafficLightService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;


@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/v1/intersections/{id}")
@Tag(name = "Traffic Light Controller", description = "Traffic light operation APIs")
public class TrafficLightController {

    @NonNull
    private TrafficLightService trafficLightService;

    @Operation(summary = "Create new intersection",
            description = "Create new intersection endpoint")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Intersection created",
                    content = @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = IntersectionDTO.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<IntersectionDTO> createIntersection(@NotBlank(message = "Intersection ID is mandatory")
                                                              @PathVariable("id") String idName) throws TrafficLightServiceException {
        return new ResponseEntity<>(trafficLightService.createIntersection(idName), HttpStatus.CREATED);
    }

    @Operation(summary = "Change intersection light",
            description = "Change intersection light endpoint to change RED, YELLOW & GREEN light")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Intersection light changed",
                    content = @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = IntersectionDTO.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @PostMapping(path = "/changelight", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<IntersectionDTO> changeLight(@NotBlank(message = "Intersection ID is mandatory") @PathVariable("id") String idName,
                                                       @NotBlank(message = "Direction is mandatory") @RequestParam String direction,
                                                       @NotBlank(message = "Signal Color is mandatory") @RequestParam String color)
            throws TrafficLightServiceException {
        return new ResponseEntity<>(trafficLightService.changeLight(idName, Direction.getEnum(direction), LightColor.getEnum(color)), HttpStatus.OK);
    }

    @Operation(summary = "Update intersection state",
            description = "Update intersection state endpoint to pause (PAUSED) or resume (RUNNING) light in intersection")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Intersection light could paused or resumed",
                    content = @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = IntersectionDTO.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @PostMapping(path = "/{state}light", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<IntersectionDTO> updateIntersectionState(@NotBlank(message = "Intersection ID is mandatory") @PathVariable("id") String idName,
                                                                   @NotBlank(message = "State is mandatory") @PathVariable String state)
            throws TrafficLightServiceException {
        return new ResponseEntity<>(trafficLightService.updateIntersectionState(idName, IntersectionAutoRunStatus.getEnum(state)), HttpStatus.OK);
    }

    @Operation(summary = "Get intersection light state",
            description = "Get intersection light state endpoint to get intersection light details")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Get intersection light details",
                    content = @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = IntersectionDTO.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @GetMapping(path = "/lightstate", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<IntersectionDTO> getIntersectionState(@NotBlank(message = "Intersection ID is mandatory") @PathVariable("id") String idName)
            throws TrafficLightServiceException {
        return new ResponseEntity<>(trafficLightService.getIntersectionState(idName), HttpStatus.OK);
    }

    @Operation(summary = "Get intersection light history",
            description = "Get intersection light history endpoint to get intersection light history in pagination")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Get paginated intersection light history",
                    content = @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = LightHistoryPageDTO.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @GetMapping(path = "/lighthistory", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LightHistoryPageDTO> getLightHistory(@NotBlank(message = "Intersection ID is mandatory") @PathVariable("id") String idName,
                                                               @NotNull(message = "Page Number is mandatory") @RequestParam Integer pageNumber,
                                                               @NotNull(message = "Page Size is mandatory") @RequestParam Integer pageSize)
            throws TrafficLightServiceException {
        return new ResponseEntity<>(trafficLightService.getLightHistory(idName, pageNumber, pageSize), HttpStatus.OK);
    }
}
