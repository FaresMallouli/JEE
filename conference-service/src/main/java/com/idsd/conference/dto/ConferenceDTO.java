package com.idsd.conference.dto; // Correct package

import com.idsd.conference.entity.ConferenceType; // Correct import
import java.time.LocalDate;
import java.util.List;

public record ConferenceDTO(
    Long id,
    String titre,
    ConferenceType type,
    LocalDate date,
    int duree,
    int nombreInscrits,
    double score,
    Long keynoteId,
    List<ReviewDTO> reviews // Include reviews in the full DTO
) {}