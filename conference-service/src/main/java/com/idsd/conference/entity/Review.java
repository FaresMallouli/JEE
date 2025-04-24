package com.idsd.conference.entity; // Correct package

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.ToString;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Review {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate date = LocalDate.now();

    @Column(length = 1000)
    private String texte;

    @Min(1) @Max(5) // Basic validation
    private int note; // 1-5

    @ManyToOne(fetch = FetchType.LAZY, optional = false) // Review must belong to a conference
    @JoinColumn(name = "conference_id", nullable = false)
    @ToString.Exclude // Avoid recursion in toString
    @EqualsAndHashCode.Exclude // Avoid recursion in equals/hashCode
    private Conference conference;
}