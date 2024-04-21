package com.kartishan.bookscroll.repository;

import com.kartishan.bookscroll.model.Book;
import com.kartishan.bookscroll.model.User;
import com.kartishan.bookscroll.model.UserBookPreference;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserBookPreferenceRepository extends JpaRepository<UserBookPreference, UUID> {
    Optional<UserBookPreference> findByUserAndBook(User user, Book book);

    List<UserBookPreference> findByBook(Book book);

    List<UserBookPreference> findByUser(User user);
}
