package com.kartishan.bookscroll.repository;

import com.kartishan.bookscroll.model.Book;
import com.kartishan.bookscroll.model.BookChapter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BookChapterRepository extends JpaRepository<BookChapter, UUID> {


    Optional<BookChapter> findByBookAndNumber(Book book, Long chapterNumber);

    Optional<BookChapter> findByBookIdAndNumber(UUID bookId, Long chapterNumber);

    List<BookChapter> findByBookIdOrderByNumberAsc(UUID bookId);
}
