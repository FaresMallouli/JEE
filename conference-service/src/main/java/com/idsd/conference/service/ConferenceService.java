package com.idsd.conference.service; // Correct package

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// Correct imports using com.idsd...
import com.idsd.conference.client.KeynoteServiceClient;
import com.idsd.conference.dto.*;
import com.idsd.conference.entity.Conference;
import com.idsd.conference.entity.Review;
import com.idsd.conference.exception.ConferenceNotFoundException; // Define this custom exception
import com.idsd.conference.exception.KeynoteValidationException; // Define this custom exception
import com.idsd.conference.mapper.ConferenceMapper;
import com.idsd.conference.mapper.ReviewMapper;
import com.idsd.conference.repository.ConferenceRepository;
import com.idsd.conference.repository.ReviewRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j // Lombok logger
public class ConferenceService {

    private final ConferenceRepository conferenceRepository;
    private final ReviewRepository reviewRepository;
    private final ConferenceMapper conferenceMapper;
    private final ReviewMapper reviewMapper;
    private final KeynoteServiceClient keynoteServiceClient; // Feign Client

    @Transactional(readOnly = true)
    public List<ConferenceDTO> getAllConferences() {
        log.info("Fetching all conferences");
        return conferenceRepository.findAll().stream()
                .map(conferenceMapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public Optional<ConferenceDTO> getConferenceById(Long id) {
        log.info("Fetching conference by ID: {}", id);
        return conferenceRepository.findById(id).map(conferenceMapper::toDto);
    }

    @Transactional // Read/write transaction
    public ConferenceDTO createConference(CreateConferenceDTO createDto) {
        log.info("Attempting to create conference: {}", createDto.titre());
        validateKeynote(createDto.keynoteId()); // Validate keynote using Feign

        Conference conference = conferenceMapper.createDtoToEntity(createDto);
        Conference savedConference = conferenceRepository.save(conference);
        log.info("Successfully created conference with ID: {}", savedConference.getId());
        return conferenceMapper.toDto(savedConference);
    }

    @Transactional
    public ReviewDTO addReviewToConference(Long conferenceId, CreateReviewDTO createReviewDto) {
        log.info("Attempting to add review to conference ID: {}", conferenceId);
        Conference conference = conferenceRepository.findById(conferenceId)
                .orElseThrow(() -> {
                    log.error("Conference not found with ID: {}", conferenceId);
                    return new ConferenceNotFoundException("Conference not found with id: " + conferenceId);
                });

        Review review = reviewMapper.createDtoToEntity(createReviewDto);
        conference.addReview(review); // Use helper method to manage relationship

        // Saving the conference cascades the save to the new review because of CascadeType.ALL
        conferenceRepository.save(conference);
        // The review object now has its ID after being saved
        log.info("Successfully added review with ID {} to conference ID: {}", review.getId(), conferenceId);
        return reviewMapper.toDto(review);
    }

     @Transactional(readOnly = true)
     public List<ReviewDTO> getReviewsForConference(Long conferenceId) {
         log.info("Fetching reviews for conference ID: {}", conferenceId);
          if (!conferenceRepository.existsById(conferenceId)) {
               log.warn("Attempted to fetch reviews for non-existent conference ID: {}", conferenceId);
               throw new ConferenceNotFoundException("Conference not found with id: " + conferenceId);
          }
        return reviewRepository.findByConferenceId(conferenceId).stream()
                .map(reviewMapper::toDto)
                .toList();
     }

    // Helper method to validate keynote using Feign client
    private void validateKeynote(Long keynoteId) {
        if (keynoteId != null) {
            log.info("Validating keynote with ID: {}", keynoteId);
            try {
                // This call is protected by the Circuit Breaker
                KeynoteDTO keynote = keynoteServiceClient.getKeynoteById(keynoteId);
                log.debug("Received keynote data: {}", keynote); // Use debug level

                // Check if the fallback was triggered (based on placeholder data)
                if ("Unknown".equals(keynote.nom()) && "Fallback: Service Unavailable".equals(keynote.fonction())) {
                     log.error("Keynote validation failed (Fallback triggered) for ID: {}", keynoteId);
                     throw new KeynoteValidationException("Keynote service unavailable or keynote ID " + keynoteId + " is invalid (fallback).");
                }
                log.info("Successfully validated Keynote ID: {}", keynoteId);

            } catch (Exception e) { // Catch Feign exceptions or fallback exceptions
                log.error("Error validating keynote ID {}: {}", keynoteId, e.getMessage());
                 // Rethrow as a specific application exception
                 throw new KeynoteValidationException("Failed to validate keynote ID: " + keynoteId + ". Reason: " + e.getMessage(), e);
            }
        } else {
            log.info("No keynote ID provided for validation.");
            // Decide if a keynote is mandatory. If so, throw an exception here.
            // throw new KeynoteValidationException("Keynote ID is required to create a conference.");
        }
    }

     // --- Define Custom Exceptions (inner classes or separate files) ---
     public static class ConferenceNotFoundException extends RuntimeException {
         public ConferenceNotFoundException(String message) { super(message); }
     }
     public static class KeynoteValidationException extends RuntimeException {
         public KeynoteValidationException(String message) { super(message); }
         public KeynoteValidationException(String message, Throwable cause) { super(message, cause); }
     }
     // Add update/delete logic as needed
}