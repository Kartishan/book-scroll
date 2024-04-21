package com.kartishan.bookscroll.repository;

import com.kartishan.bookscroll.model.Scroll;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ScrollRepository extends JpaRepository<Scroll, UUID> {
    Optional<Scroll> findById(UUID id);
}
