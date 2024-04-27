package com.kartishan.bookscroll.repository;

import com.kartishan.bookscroll.model.Bookmark;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface BookmarkRepository extends JpaRepository<Bookmark, UUID> {
    List<Bookmark> findByUserIdAndBookId(UUID userId, UUID bookId);
}
