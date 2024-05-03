package com.kartishan.bookscroll.repository;

import com.kartishan.bookscroll.model.Book;
import com.kartishan.bookscroll.model.LastPlaybackPosition;
import com.kartishan.bookscroll.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface LastPlaybackPositionRepository extends JpaRepository<LastPlaybackPosition, UUID> {
    Optional<LastPlaybackPosition> findByUserAndBook(User user, Book book);
}
