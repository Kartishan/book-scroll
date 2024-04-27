package com.kartishan.bookscroll.controller;


import com.kartishan.bookscroll.model.Book;
import com.kartishan.bookscroll.model.User;
import com.kartishan.bookscroll.model.UserBookViewHistory;
import com.kartishan.bookscroll.model.dto.BookWithCategoriesAndHistoryDTO;
import com.kartishan.bookscroll.model.dto.BookWithCategoriesDTO;
import com.kartishan.bookscroll.service.BookService;
import com.kartishan.bookscroll.service.GridFsService;
import com.kartishan.bookscroll.service.jwt.JwtService;
import com.kartishan.bookscroll.service.jwt.UserService;
import com.mongodb.client.gridfs.model.GridFSFile;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Tag(name = "Book methods api")
@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/book")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;
    private final JwtService jwtService;
    private final UserService userService;
    private final GridFsService gridFsService;

    @Operation(
            summary = "Метод для полкчения книги по Id",
            description = "Для книги надо отрпавить Id"
    )
    @GetMapping("/{id}")
    public ResponseEntity<BookWithCategoriesDTO> getBookByIdWithCategory(@PathVariable UUID id, HttpServletRequest request) {
        BookWithCategoriesDTO bookWithCategory = bookService.getBookByIdWithCategory(id);
        addBookViewAndHistory(bookWithCategory.getId(), request);
        return ResponseEntity.ok().body(bookWithCategory);
    }

    @GetMapping("/bookFileId/{id}")
    public ResponseEntity<String> getBookFileByIdWithCategory(@PathVariable UUID id, HttpServletRequest request) {
        Book book = bookService.getBookById(id);
        return ResponseEntity.ok().body(book.getFileId());
    }

    private void addBookViewAndHistory(UUID bookId, HttpServletRequest request) {
        try {
            Book book = bookService.getBookById(bookId);
            bookService.addBookView(book);

            String authorizationHeader = request.getHeader("Authorization");
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                String token = authorizationHeader.substring(7);
                String username = jwtService.extractUsername(token);

                User user = userService.getUserByUsername(username);
                if (user != null) {
                    bookService.addBookViewHistory(user, book);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    @Operation(
            summary = "Метод для полкчения ВСЕХ книг",
            description = "Просто все книги"
    )
    @GetMapping("/all")
    public ResponseEntity<Page<BookWithCategoriesDTO>> getBooks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<BookWithCategoriesDTO> bookPage = bookService.getAllBooks(page, size);
        return ResponseEntity.ok(bookPage);
    }

    @GetMapping("/files/{fileId}")
    public ResponseEntity<InputStreamResource> downloadFile(@PathVariable String fileId) {
        InputStreamResource inputStreamResource = gridFsService.downloadFileAsResource(fileId);

        GridFSFile gridFSFile = gridFsService.retrieveFile(fileId);
        String fileName = gridFSFile.getFilename();

        String contentType = Optional.ofNullable(gridFSFile.getMetadata().get("_contentType").toString())
                .orElse("application/octet-stream");

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .contentLength(gridFSFile.getLength())
                .body(inputStreamResource);
    }

    @Operation(
            summary = "Метод для получения книг по категории",
            description = "Для получения списка книг отправьте название категории"
    )
    @GetMapping("/category/{category}")
    public ResponseEntity<Page<BookWithCategoriesDTO>> getBooksByCategory(
            @PathVariable("category") String category,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize
    ) {
        Page<BookWithCategoriesDTO> booksDTOPage = bookService.getBooksByCategory(category, page, pageSize);
        return ResponseEntity.ok(booksDTOPage);
    }

    @GetMapping("/top-rated")
    public ResponseEntity<Page<BookWithCategoriesDTO>> getTopRatedBooks(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        Page<BookWithCategoriesDTO> books = bookService.getTopRatedBooks(page, pageSize);
        return ResponseEntity.ok(books);
    }

    @GetMapping("/top-viewed")
    public ResponseEntity<Page<BookWithCategoriesDTO>> getTopViewedBooks(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        Page<BookWithCategoriesDTO> books = bookService.getTopViewedBooks(page, pageSize);
        return ResponseEntity.ok(books);
    }

    @Operation(
            summary = "Метод для поиска книги по названию",
            description = "Для книги надо отрпавить название книги"
    )

    @GetMapping("/search/partialName/{partialName}")
    public ResponseEntity<Page<Book>> getBooksByPartialName(
            @PathVariable("partialName") String partialName,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size) {
        Page<Book> books = bookService.getBooksByPartialName(partialName, page, size);
        return ResponseEntity.ok(books);
    }

    @GetMapping("/history")
    public ResponseEntity<List<BookWithCategoriesAndHistoryDTO>> getUserHistory(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        UUID UserId = null;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            String username = jwtService.extractUsername(token);

            User user = userService.getUserByUsername(username);
            if (user != null) {
                UserId = user.getId();
            }
        }
        List<BookWithCategoriesAndHistoryDTO> userHistory = bookService.getUserHistory(UserId);
        return ResponseEntity.ok(userHistory);
    }

}