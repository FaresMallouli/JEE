package com.idsd.keynote; // Correct package

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient // Register with Eureka
@OpenAPIDefinition(info = @Info(title = "Keynote API", version = "1.0", description = "Manage Keynotes"))
public class KeynoteServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(KeynoteServiceApplication.class, args);
    }
}