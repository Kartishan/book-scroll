package com.kartishan.bookscroll.service;

import com.kartishan.bookscroll.model.Book;
import com.kartishan.bookscroll.model.Category;
import com.kartishan.bookscroll.model.User;
import com.kartishan.bookscroll.model.UserBookPreference;
import com.kartishan.bookscroll.repository.BookRepository;
import com.kartishan.bookscroll.repository.CategoryRepository;
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
public class RecommendationService {
    private final UserBookPreferenceRepository userBookPreferenceRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    public double calculateSimilarityBetweenBooks(Book book1, Book book2) {
        Set<Category> categoriesBook1 = categoryRepository.findCategoriesByBookId(book1.getId());
        Set<Category> categoriesBook2 = categoryRepository.findCategoriesByBookId(book2.getId());

        categoriesBook1.retainAll(categoriesBook2);
        return (!categoriesBook1.isEmpty()) ? 1.0 : 0.0;
    }
    public double calculatePredictedRatingForBook(Book book, double similarityThreshold) {
        List<User> allUsers = userRepository.findAll();
        double totalRating = 0;
        int count = 0;

        for (User user : allUsers) {
            List<UserBookPreference> userPreferences = userBookPreferenceRepository.findByUser(user);
            for (UserBookPreference preference : userPreferences) {
                if (preference.getBook().equals(book)) {
                    double similarity = calculateSimilarityBetweenBooks(book, preference.getBook());
                    if (similarity >= similarityThreshold) {
                        totalRating += preference.getRating() * similarity;
                        count++;
                    }
                }
            }
        }

        return count > 0 ? totalRating / count : 0;
    }
    public List<Book> recommendSimilarBooks(UUID bookId, int n) {
        Optional<Book> selectedBook = bookRepository.findById(bookId);
        if (selectedBook.isEmpty()) {
            return Collections.emptyList();
        }

        Book book = selectedBook.get();

        Map<Book, Double> predictedRatings = new HashMap<>();
        List<Book> allBooks = bookRepository.findAll();
        for (Book otherBook : allBooks) {
            if (!otherBook.equals(book)) {
                double similarity = calculateSimilarityBetweenBooks(book, otherBook);
                if (similarity > 0) {
                    double rating = calculatePredictedRatingForBook(otherBook, similarity);
                    predictedRatings.put(otherBook, rating);
                }
            }
        }

        List<Book> recommendedBooks = predictedRatings.entrySet().stream()
                .sorted(Map.Entry.<Book, Double>comparingByValue().reversed())
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        return recommendedBooks.subList(0, Math.min(n, recommendedBooks.size()));
    }

    public double calculateUserSimilarity(User user1, User user2) {
        List<UserBookPreference> user1Preferences = userBookPreferenceRepository.findByUser(user1);
        List<UserBookPreference> user2Preferences = userBookPreferenceRepository.findByUser(user2);

        Map<Book, Integer> user1Ratings = new HashMap<>();
        Map<Book, Integer> user2Ratings = new HashMap<>();

        for (UserBookPreference preference : user1Preferences) {
            user1Ratings.put(preference.getBook(), preference.getRating());
        }

        for (UserBookPreference preference : user2Preferences) {
            user2Ratings.put(preference.getBook(), preference.getRating());
        }

        double dotProduct = 0;
        double normUser1 = 0;
        double normUser2 = 0;

        for (Book book : user1Ratings.keySet()) {
            int user1Rating = user1Ratings.getOrDefault(book, 0);
            int user2Rating = user2Ratings.getOrDefault(book, 0);

            dotProduct += user1Rating * user2Rating;
            normUser1 += Math.pow(user1Rating, 2);
            normUser2 += Math.pow(user2Rating, 2);
        }

        if (normUser1 == 0 || normUser2 == 0) {
            return 0;
        }

        return dotProduct / (Math.sqrt(normUser1) * Math.sqrt(normUser2));
    }

    public Map<Book, Double> predictRatingsForUser(User user) {
        Map<Book, Double> predictedRatings = new HashMap<>();

        List<User> allUsers = userRepository.findAll();
        for (User otherUser : allUsers) {
            if (!otherUser.equals(user)) {
                double similarity = calculateUserSimilarity(user, otherUser);
                if (similarity > 0) {
                    List<UserBookPreference> otherUserPreferences = userBookPreferenceRepository.findByUser(otherUser);
                    for (UserBookPreference preference : otherUserPreferences) {
                        Book book = preference.getBook();
                        if (!userBookPreferenceRepository.findByUserAndBook(user, book).isPresent()) {
                            double rating = preference.getRating();
                            predictedRatings.put(book, predictedRatings.getOrDefault(book, 0.0) + rating * similarity);
                        }
                    }
                }
            }
        }

        return predictedRatings;
    }
}
