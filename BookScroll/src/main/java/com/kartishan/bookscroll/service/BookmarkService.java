package com.kartishan.bookscroll.service;

import com.kartishan.bookscroll.model.Book;
import com.kartishan.bookscroll.model.Bookmark;
import com.kartishan.bookscroll.model.User;
import com.kartishan.bookscroll.repository.BookRepository;
import com.kartishan.bookscroll.repository.BookmarkRepository;
import com.kartishan.bookscroll.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;

    public List<Bookmark> getBookmarksByUserAndBook(UUID userId, UUID bookId) {
        return bookmarkRepository.findByUserIdAndBookId(userId, bookId);
    }

    public Bookmark saveBookmark(UUID userId, UUID bookId, String cfiRange, String text,String comment) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден"));
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new RuntimeException("Книга не найдена"));

        Bookmark bookmark = Bookmark.builder()
                .user(user)
                .book(book)
                .cfiRange(cfiRange)
                .comment(comment)
                .text(text)
                .build();

        return bookmarkRepository.save(bookmark);
    }

    public void deleteBookmark(UUID bookmarkId) {
        bookmarkRepository.deleteById(bookmarkId);
    }

}
