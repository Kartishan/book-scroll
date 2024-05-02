package com.kartishan.bookscroll.service;

import com.kartishan.bookscroll.model.Book;
import com.kartishan.bookscroll.model.BookChapter;
import com.kartishan.bookscroll.repository.BookChapterRepository;
import com.kartishan.bookscroll.repository.BookRepository;
import com.kartishan.bookscroll.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class AudioService {
    private final BookRepository bookRepository;
    private final GridFsService gridFsService;
    private final BookChapterRepository bookChapterRepository;

    public boolean uploadAudioChapter(UUID bookId, Long chapterNumber, MultipartFile audioFile) {
        Optional<Book> bookOptional = bookRepository.findById(bookId);

        if (!bookOptional.isPresent()) {
            log.error("Book with ID: " + bookId + " not found.");
            return false;
        }

        Optional<BookChapter> chapterOptional = bookChapterRepository.findByBookAndNumber(bookOptional.get(), chapterNumber);

        BookChapter chapter;
        if (chapterOptional.isPresent()) {
            chapter = chapterOptional.get();
            log.info("Updating existing chapter with number: " + chapterNumber + " for book with ID: " + bookId);
        } else {
            chapter = BookChapter.builder()
                    .book(bookOptional.get())
                    .number(chapterNumber)
                    .build();
            log.info("Creating new chapter with number: " + chapterNumber + " for book with ID: " + bookId);
        }

        try {
            String fileId = gridFsService.storeFile(audioFile);
            chapter.setAudioFileId(fileId);
            bookChapterRepository.save(chapter);
            log.info("Audio file for chapter " + chapterNumber + " successfully uploaded and associated with book with ID: " + bookId);
            return true;
        } catch (IOException e) {
            log.error("Failed to upload audio file for chapter " + chapterNumber + ": " + e.getMessage(), e);
            return false;
        }
    }

}
