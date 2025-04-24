package com.idsd.conference.entity; // Correct package

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Conference {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titre;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ConferenceType type;

    @Column(nullable = false)
    private LocalDate date;

    private int duree; // minutes?

    private int nombreInscrits = 0;

    private double score = 0.0;

    // Store the ID of the keynote. Fetch details via Feign.
    @Column(nullable = true) // Allow conferences without keynotes initially? Or make false?
    private Long keynoteId;

    // Cascade ALL: if conference is saved/deleted, reviews are too.
    // orphanRemoval: if a review is removed from this list, it's deleted from DB.
    @OneToMany(mappedBy = "conference", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Review> reviews = new ArrayList<>();

    // Helper method for bidirectional relationship management
    public void addReview(Review review) {
        if (review != null) {
            reviews.add(review);
            review.setConference(this);
        }
    }

    public void removeReview(Review review) {
         if (review != null) {
            reviews.remove(review);
            review.setConference(null);
         }
    }

    // Custom equals and hashCode to avoid issues with lazy-loaded collections
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Conference that = (Conference) o;
        return Objects.equals(id, that.id); // Use ID for equality if available
    }

    @Override
    public int hashCode() {
        return Objects.hash(id); // Use ID for hashcode
    }
}