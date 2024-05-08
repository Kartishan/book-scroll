package com.kartishan.bookscroll.service;

import com.kartishan.bookscroll.model.Book;
import com.kartishan.bookscroll.model.BookChapter;
import com.kartishan.bookscroll.model.LastPlaybackPosition;
import com.kartishan.bookscroll.model.User;
import com.kartishan.bookscroll.repository.BookChapterRepository;
import com.kartishan.bookscroll.repository.BookRepository;
import com.kartishan.bookscroll.repository.LastPlaybackPositionRepository;
import com.kartishan.bookscroll.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LastPlaybackPositionService {

    private final LastPlaybackPositionRepository lastPlaybackPositionRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final BookChapterRepository bookChapterRepository;

    public LastPlaybackPosition updatePlaybackPosition(UUID userId, UUID bookId, Long chapterNumber, double position) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new RuntimeException("Book not found"));
        System.out.println(bookId);
        System.out.println(chapterNumber);
        BookChapter chapter = bookChapterRepository.findByBookIdAndNumber(bookId, chapterNumber).orElseThrow(() -> new RuntimeException("Chapter not found"));

        LastPlaybackPosition playbackPosition = lastPlaybackPositionRepository
                .findByUserAndBook(user, book)
                .orElse(new LastPlaybackPosition());

        playbackPosition.setUser(user);
        playbackPosition.setBook(book);
        playbackPosition.setChapter(chapter);
        playbackPosition.setLastPlaybackPosition(position);

        return lastPlaybackPositionRepository.save(playbackPosition);
    }
    public LastPlaybackPosition getPlaybackPosition(UUID userId, UUID bookId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new RuntimeException("Book not found"));

        return lastPlaybackPositionRepository.findByUserAndBook(user, book)
                .orElseThrow(() -> new RuntimeException("Playback position not found"));
    }
    public LastPlaybackPosition getLastPlaybackPositionForUser(UUID userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        return lastPlaybackPositionRepository.findFirstByUserIdOrderByUpdatedAtDesc(user.getId())
                .orElseThrow(() -> new RuntimeException("Playback position not found"));
    }
}
