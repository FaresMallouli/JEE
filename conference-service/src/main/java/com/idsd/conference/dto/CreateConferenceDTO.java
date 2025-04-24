package com.idsd.conference.dto; // Correct package

import com.idsd.conference.entity.ConferenceType; // Correct import
import java.time.LocalDate;
import jakarta.validation.constraints.NotBlank; // Example using validation
import jakarta.validation.constraints.NotNull;

// DTO specifically for creating a conference
public record CreateConferenceDTO(
    @NotBlank String titre, // Add validation example
    @NotNull ConferenceType type,
    @NotNull LocalDate date,
    int duree,
    Long keynoteId // Nullable? Depends on business logic
) {}