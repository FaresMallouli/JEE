package com.idsd.keynote.repository; // Correct package

import org.springframework.data.jpa.repository.JpaRepository;
import com.idsd.keynote.entity.Keynote; // Correct import

public interface KeynoteRepository extends JpaRepository<Keynote, Long> { }