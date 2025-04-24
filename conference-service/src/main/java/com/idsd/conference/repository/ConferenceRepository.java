package com.idsd.conference.repository; // Correct package

import org.springframework.data.jpa.repository.JpaRepository;
import com.idsd.conference.entity.Conference; // Correct import

public interface ConferenceRepository extends JpaRepository<Conference, Long> {}