package com.idsd.conference.dto; // Correct package

import java.time.LocalDate;

public record ReviewDTO(
    Long id,
    LocalDate date,
    String texte,
    int note
) {}