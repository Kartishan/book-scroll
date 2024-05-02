package com.kartishan.bookscroll.controller;


import com.kartishan.bookscroll.model.BookChapter;
import com.kartishan.bookscroll.repository.BookChapterRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/chapters")
@RequiredArgsConstructor
public class BookChapterController {
    private final BookChapterRepository bookChapterRepository;

    @GetMapping("/{bookId}/{chapterNumber}/audio-id")
    public ResponseEntity<String> getAudioFileId(@PathVariable UUID bookId, @PathVariable Long chapterNumber) {
        Optional<BookChapter> chapter = bookChapterRepository.findByBookIdAndNumber(bookId, chapterNumber);
        if (chapter.isPresent()) {
            return ResponseEntity.ok(chapter.get().getAudioFileId());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{bookId}/{chapterNumber}/summary")
    public ResponseEntity<String> getChapterSummary(@PathVariable UUID bookId, @PathVariable Long chapterNumber) {
        Optional<BookChapter> chapter = bookChapterRepository.findByBookIdAndNumber(bookId, chapterNumber);
        if (chapter.isPresent()) {
            return ResponseEntity.ok(chapter.get().getSummary());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
