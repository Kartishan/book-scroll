package com.kartishan.bookscroll.service;

import com.kartishan.bookscroll.model.Book;
import com.kartishan.bookscroll.model.BookView;
import com.kartishan.bookscroll.model.User;
import com.kartishan.bookscroll.model.UserBookPreference;
import com.kartishan.bookscroll.repository.BookRepository;
import com.kartishan.bookscroll.repository.UserBookPreferenceRepository;
import com.kartishan.bookscroll.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserBookPreferenceService {
    private final UserBookPreferenceRepository userBookPreferenceRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    public OptionalDouble calculateAverageRatingForBook(Book book) {
        List<UserBookPreference> userBookPreferences = userBookPreferenceRepository.findByBook(book);
        return userBookPreferences.stream()
                .mapToInt(UserBookPreference::getRating)
                .average();
    }
    public void updateUserBookPreference(UUID userId, UUID bookId, int rating) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new IllegalArgumentException("Book not found"));

        UserBookPreference userBookPreference = userBookPreferenceRepository.findByUserAndBook(user, book)
                .orElseGet(() -> {
                    UserBookPreference newUserBookPreference = new UserBookPreference();
                    newUserBookPreference.setUser(user);
                    newUserBookPreference.setBook(book);
                    return newUserBookPreference;
                });
        userBookPreference.setRating(rating);
        userBookPreferenceRepository.save(userBookPreference);
        updateBookRating(book);
    }

    private void updateBookRating(Book book) {
        OptionalDouble averageRating = calculateAverageRatingForBook(book);
        double newRating = averageRating.orElse(0);
        book.setRating(newRating);
        bookRepository.save(book);
    }
}
