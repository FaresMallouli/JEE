package com.idsd.keynote.dto; // Correct package

// Using record for immutable DTO
public record KeynoteDTO(
    Long id,
    String nom,
    String prenom,
    String email,
    String fonction
) {}