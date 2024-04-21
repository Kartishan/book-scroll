package com.kartishan.bookscroll.controller;

import com.kartishan.bookscroll.model.Book;
import com.kartishan.bookscroll.service.RecommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/recommendation")
@RequiredArgsConstructor
public class RecommendationController {
    private final RecommendationService recommendationService;
    @GetMapping("/forBook")
    public ResponseEntity<List<Book>> recommendSimilarBooks(@RequestParam UUID bookId, @RequestParam int n) {
        List<Book> recommendedBooks = recommendationService.recommendSimilarBooks(bookId, n);
        return ResponseEntity.ok(recommendedBooks);
    }
}
