package com.kartishan.bookscroll.repository;

import com.kartishan.bookscroll.model.UserBookViewHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface UserBookViewHistoryRepository extends JpaRepository<UserBookViewHistory, UUID> {
    List<UserBookViewHistory> findByUserId(UUID userId);
}
