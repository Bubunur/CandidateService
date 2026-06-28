package com.tunduk.candidateservice.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Candidate Service API")
                        .description("REST API микросервиса управления кандидатами платформы CV-Scan")
                        .version("1.0.0"))
                .servers(List.of(
                        new Server().url("http://localhost:8090")
                ));
    }
}
