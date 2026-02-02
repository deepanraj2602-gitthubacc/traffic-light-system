
package com.traffic.system.trafficlightservice.enums;

import java.util.Arrays;

public enum Direction {
    NORTH, SOUTH, EAST, WEST;

    public static Direction getEnum(String directionStr) {
        return Arrays.stream(Direction.values()).filter(dir -> dir.name().equals(directionStr))
                .findFirst().orElseThrow();
    }
}
