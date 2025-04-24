package com.idsd.conference.controller; // Correct package

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid; // Enable validation
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// Correct imports using com.idsd...
import com.idsd.conference.dto.*;
import com.idsd.conference.service.ConferenceService;
import com.idsd.conference.service.ConferenceService.ConferenceNotFoundException; // Import custom exceptions
import com.idsd.conference.service.ConferenceService.KeynoteValidationException;

import java.util.List;

@RestController
@RequestMapping("/conferences") // Base path used by Gateway routing
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Conference API", description = "Endpoints for Conferences & Reviews")
public class ConferenceController {

    private final ConferenceService conferenceService;

    @GetMapping
    @Operation(summary = "Get all conferences")
    public List<ConferenceDTO> getAll() {
        return conferenceService.getAllConferences();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get conference by ID")
    public ResponseEntity<ConferenceDTO> getById(@PathVariable Long id) {
        return conferenceService.getConferenceById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Create a new conference")
    // Add @Valid to trigger bean validation on the DTO
    public ResponseEntity<?> create(@Valid @RequestBody CreateConferenceDTO createDto) {
        try {
            ConferenceDTO savedDto = conferenceService.createConference(createDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedDto);
        // Catch specific exceptions from the service layer for better error responses
        } catch (KeynoteValidationException e) {
            log.warn("Conference creation failed due to keynote validation: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) { // Catch other potential errors
             log.error("Unexpected error during conference creation: {}", e.getMessage(), e);
             return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
        }
    }

    // --- Review Endpoints ---

    @PostMapping("/{conferenceId}/reviews")
    @Operation(summary = "Add a review to a conference")
    public ResponseEntity<?> addReview(
            @PathVariable Long conferenceId,
            @Valid @RequestBody CreateReviewDTO createReviewDto) { // Enable validation
        try {
            ReviewDTO savedReview = conferenceService.addReviewToConference(conferenceId, createReviewDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedReview);
        } catch (ConferenceNotFoundException e) {
            log.warn("Failed to add review: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            log.error("Unexpected error adding review to conference {}: {}", conferenceId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
        }
    }

    @GetMapping("/{conferenceId}/reviews")
    @Operation(summary = "Get all reviews for a specific conference")
    public ResponseEntity<?> getReviews(@PathVariable Long conferenceId) {
         try {
            List<ReviewDTO> reviews = conferenceService.getReviewsForConference(conferenceId);
            return ResponseEntity.ok(reviews);
         } catch (ConferenceNotFoundException e) {
             log.warn("Failed to get reviews: {}", e.getMessage());
             return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
         } catch (Exception e) {
             log.error("Unexpected error getting reviews for conference {}: {}", conferenceId, e.getMessage(), e);
             return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
         }
    }

     // TODO: Add endpoints for Update/Delete Conferences and Reviews if required by exam
}