package com.kartishan.bookscroll.controller;

import com.kartishan.bookscroll.model.Book;
import com.kartishan.bookscroll.request.BookRequest;
import com.kartishan.bookscroll.service.BookService;
import com.kartishan.bookscroll.service.ManagerBookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Tag(name = "Manager book methods api")
@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/manager/book")
@RequiredArgsConstructor
public class ManagerBookController {
    private final ManagerBookService managerBookService;

    @Operation(      summary = "Метод для полкчения книги по Id",
            description = "Для книги надо отрпавить Id")
    @PostMapping("/add")
    public ResponseEntity<String> createBooks(@RequestBody BookRequest request){
        managerBookService.createBook(request);
        return ResponseEntity.status(HttpStatus.CREATED).body("Книга была добавлена.");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> changeBooks(@PathVariable("book") Book book){
        managerBookService.changeBookInformation(book);
        return ResponseEntity.ok("Книга успешно изменена");
    }
}
