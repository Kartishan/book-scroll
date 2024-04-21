package com.kartishan.bookscroll.service;


import com.kartishan.bookscroll.exceptions.BookNotFoundException;
import com.kartishan.bookscroll.model.*;
import com.kartishan.bookscroll.model.dto.BookWithCategoriesDTO;
import com.kartishan.bookscroll.repository.BookRepository;
import com.kartishan.bookscroll.repository.BookViewRepository;
import com.kartishan.bookscroll.repository.CategoryRepository;
import com.kartishan.bookscroll.repository.UserBookViewHistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class BookService {
    private final BookRepository bookRepository;
    private final BookViewRepository bookViewRepository;
    private final UserBookViewHistoryRepository userBookViewHistoryRepository;
    private final CategoryRepository categoryRepository;
    public Book getBookById(UUID id){
        return bookRepository.findById(id)
                .orElseThrow(()-> new BookNotFoundException("Book with id " + id + " not found"));
    }
    public BookWithCategoriesDTO getBookByIdWithCategory(UUID id){
        Book book = getBookById(id);
        return convertToDto(book);
    }

    public BookWithCategoriesDTO convertToDto(Book book) {
        Set<Category> categories = categoryRepository.findCategoriesByBookId(book.getId());

        Set<String> categoryNames = categories.stream()
                .map(Category::getName)
                .collect(Collectors.toSet());

        BookWithCategoriesDTO bookDTO = BookWithCategoriesDTO.builder()
                .id(book.getId())
                .name(book.getName())
                .author(book.getAuthor())
                .authorFull(book.getAuthorFull())
                .description(book.getDescription())
                .rating(book.getRating())
                .pageCount(book.getPageCount())
                .categories(categoryNames)
                .build();
        return bookDTO;
    }
    public void addBookView(Book book){
        BookView bookView = bookViewRepository.findByBookId(book.getId())
                .orElseGet(() -> {
                    BookView newBookView = new BookView();
                    newBookView.setBook(book);
                    newBookView.setViewCount(0L);
                    return newBookView;
                });

        bookView.setViewCount(bookView.getViewCount() + 1);

        bookViewRepository.save(bookView);
    }

    public void addBookViewHistory(User user, Book book){
        UserBookViewHistory history = new UserBookViewHistory();
        history.setUser(user);
        history.setBook(book);
        history.setViewTime(new Date());
        userBookViewHistoryRepository.save(history);
    }
    public List<UserBookViewHistory> getUserHistory(UUID userId){
        try {
            return (userBookViewHistoryRepository.findByUserId(userId));
        }catch (Exception e){
            throw new RuntimeException("History not found! More: " + e);
        }
    }

    public Book getBookByName(String name){
        return bookRepository.findByName(name)
                .orElseThrow(()-> new BookNotFoundException("Book with name " + name + " not found"));
    }

    public Page<BookWithCategoriesDTO> getAllBooks(Integer page, Integer pageSize) {
        try {
            Pageable pageable = PageRequest.of(page, pageSize);
            Page<Book> bookPage = bookRepository.findAll(pageable);
            Page<BookWithCategoriesDTO> dtoPage = bookPage.map(this::convertToDto);
            return dtoPage;
        } catch (Exception e) {
            throw new BookNotFoundException("Books not found! More: " + e);
        }
    }

    public Page<Book> getBooksByPartialName(String partialName, Integer page, Integer  pageSize){
        try{
            Pageable pageable = PageRequest.of(page, pageSize);
            return bookRepository.findByNameContaining(partialName, pageable);
        }catch (Exception e){
            throw new BookNotFoundException("Books by partial name " + partialName + " not found");
        }
    }
    public Page<BookWithCategoriesDTO> getBooksByCategory(String category, Integer page, Integer pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        try {
            Page<Book> booksPage = bookRepository.findByCategory(category, pageable);
            return booksPage.map(this::convertToDto);
        } catch (Exception e) {
            throw new BookNotFoundException("Books by category " + category + " not found");
        }
    }

    public Page<BookWithCategoriesDTO> getTopRatedBooks(int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "rating"));
        Page<Book> booksPage = bookRepository.findAll(pageable);
        return booksPage.map(this::convertToDto);
    }

    public Page<BookWithCategoriesDTO> getTopViewedBooks(int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "viewCount"));
        Page<BookView> bookViewPage = bookViewRepository.findAllByOrderByViewCountDesc(pageable);
        return bookViewPage.map(bookView -> convertToDto(bookView.getBook()));
    }

    public List<Book> getTopBooksByRatingAndViewCount(int limit) {

        List<Book> books = bookRepository.findAll();

        Map<Book, Long> viewCounts = userBookViewHistoryRepository.findAll()
                .stream()
                .collect(Collectors.groupingBy(UserBookViewHistory::getBook, Collectors.counting()));

        Map<Book, Double> averageValues = new HashMap<>();
        for (Book book : books) {
            Long viewCount = viewCounts.getOrDefault(book, 0L);
            double rating = book.getRating();

            // Считаем среднее значение
            double averageValue = (viewCount + rating) / 2.0;
            averageValues.put(book, averageValue);
        }

        return averageValues.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Collections.reverseOrder()))
                .map(Map.Entry::getKey)
                .limit(limit)
                .collect(Collectors.toList());
    }

}
