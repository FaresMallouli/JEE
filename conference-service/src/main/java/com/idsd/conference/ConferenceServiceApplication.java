package com.idsd.conference; // Correct package

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient  // Register with Eureka
@EnableFeignClients     // Enable Feign Client scanning (scans current package and sub-packages)
@OpenAPIDefinition(info = @Info(title = "Conference API", version = "1.0", description = "Manage Conferences & Reviews"))
public class ConferenceServiceApplication {
     public static void main(String[] args) {
        SpringApplication.run(ConferenceServiceApplication.class, args);
    }
}