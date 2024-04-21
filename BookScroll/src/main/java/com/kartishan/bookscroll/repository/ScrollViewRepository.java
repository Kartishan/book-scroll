package com.kartishan.bookscroll.repository;

import com.kartishan.bookscroll.model.ScrollView;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ScrollViewRepository extends JpaRepository<ScrollView, UUID>
{
    Optional<ScrollView> findByScrollId(UUID scrollId);
}
