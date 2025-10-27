package com.tempocargo.app.tempo_cargo_api.security.v1.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI tempoCargoOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("TempoCargo API")
                        .description("REST API documentation for TempoCargo system")
                        .version("1.0.0")
                        .termsOfService("https://tempocargo.com/terms")
                );
    }
}
