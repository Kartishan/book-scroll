package com.kartishan.bookscroll.service;

import com.kartishan.bookscroll.model.BookChapter;
import com.kartishan.bookscroll.model.dto.BookChapterDTO;
import com.kartishan.bookscroll.repository.BookChapterRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class BookChapterService {
    private final BookChapterRepository bookChapterRepository;

    public List<BookChapterDTO> getChaptersByBookId(UUID bookId) {
        List<BookChapter> chapters = bookChapterRepository.findByBookIdOrderByNumberAsc(bookId);
        return chapters.stream()
                .map(chapter -> BookChapterDTO.builder()
                        .number(chapter.getNumber())
                        .audioFileId(chapter.getAudioFileId())
                        .summary(chapter.getSummary())
                        .duration(chapter.getDuration())
                        .build())
                .collect(Collectors.toList());
    }
}
