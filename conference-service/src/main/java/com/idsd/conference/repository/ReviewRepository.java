package com.idsd.conference.repository; // Correct package

import org.springframework.data.jpa.repository.JpaRepository;
import com.idsd.conference.entity.Review; // Correct import
import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
   // Custom query method to find reviews by conference ID
   List<Review> findByConferenceId(Long conferenceId);
}