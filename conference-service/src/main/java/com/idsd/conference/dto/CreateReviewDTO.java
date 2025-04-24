package com.idsd.conference.dto; // Correct package

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

// DTO specifically for creating a review
public record CreateReviewDTO(
    @NotBlank String texte,
    @Min(1) @Max(5) int note
) {}