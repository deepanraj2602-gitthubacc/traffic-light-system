/*
 * Copyright (c) 2025 Deepanraj Devaraj (deepanraj.devaraj@aspiresys.com)
 * All rights reserved.,
 *
 * This source code is proprietary. Redistribution or use without explicit
 * permission from the author is strictly prohibited.
 */
package com.traffic.system.trafficlightservice.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    private static final String SECURITY_SCHEME_NAME = "bearerAuth";

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("traffic-light-service")
                        .version("0.0.1")
                        .description("traffic light service apis"));
    }
}
