
package com.traffic.system.trafficlightservice.enums;

import java.util.Arrays;

public enum LightColor {
    RED, GREEN, YELLOW;

    public static LightColor getEnum(String colorStr) {
        return Arrays.stream(LightColor.values()).filter(color -> color.name().equals(colorStr))
                .findFirst().orElseThrow();
    }
}
