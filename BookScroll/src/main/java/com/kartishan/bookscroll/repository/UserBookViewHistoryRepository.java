package com.kartishan.bookscroll.repository;

import com.kartishan.bookscroll.model.Book;
import com.kartishan.bookscroll.model.User;
import com.kartishan.bookscroll.model.UserBookViewHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserBookViewHistoryRepository extends JpaRepository<UserBookViewHistory, UUID> {
    List<UserBookViewHistory> findByUserId(UUID userId);

    Optional<UserBookViewHistory> findByUserAndBook(User user, Book book);
}
