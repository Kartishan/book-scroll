package com.kartishan.bookscroll.repository.newBookMark;

import com.kartishan.bookscroll.model.Book;
import com.kartishan.bookscroll.model.User;
import com.kartishan.bookscroll.model.newBookMark.UserBookMark;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserBookMarkRepository extends JpaRepository<UserBookMark, UUID> {
    Optional<UserBookMark> findByUserAndBook(User user, Book book);

    Optional<UserBookMark> findByUserIdAndBookId(UUID userId, UUID bookId);
}
