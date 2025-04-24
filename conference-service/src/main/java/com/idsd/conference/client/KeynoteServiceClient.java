package com.idsd.conference.client; // Correct package

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

// Define the DTO expected from keynote-service locally for compile-time safety.
// In a real app, this often lives in a shared library.
record KeynoteDTO(Long id, String nom, String prenom, String email, String fonction) {}

// name = Eureka service ID of the target service (must match keynote-service's spring.application.name)
// path = Base path from the target controller's @RequestMapping
@FeignClient(name = "KEYNOTE-SERVICE", path = "/keynotes")
public interface KeynoteServiceClient {

    Logger log = LoggerFactory.getLogger(KeynoteServiceClient.class);

    @GetMapping("/{id}")
    @CircuitBreaker(name = "keynoteServiceBreaker", fallbackMethod = "getKeynoteFallback")
    KeynoteDTO getKeynoteById(@PathVariable("id") Long id);

    // Fallback method must be default (if in interface), have same signature + Throwable
    default KeynoteDTO getKeynoteFallback(Long id, Throwable throwable) {
        log.error("Circuit Breaker Fallback: Failed to get keynote ID {}. Reason: {}", id, throwable.getMessage());
        // Return a placeholder DTO, NOT null.
        return new KeynoteDTO(id, "Unknown", "Speaker", "N/A", "Fallback: Service Unavailable");
    }
}