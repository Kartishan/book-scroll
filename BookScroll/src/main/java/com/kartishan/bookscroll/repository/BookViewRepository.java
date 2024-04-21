package com.kartishan.bookscroll.repository;

import com.kartishan.bookscroll.model.BookView;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface BookViewRepository extends JpaRepository<BookView, UUID> {
    Optional<BookView> findByBookId(UUID bookId);

    Page<BookView> findAllByOrderByViewCountDesc(Pageable pageable);
}
