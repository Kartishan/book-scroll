package com.kartishan.bookscroll.controller;

import com.kartishan.bookscroll.model.User;
import com.kartishan.bookscroll.model.dto.NewBookmarkDTO;
import com.kartishan.bookscroll.model.newBookMark.BookmarkDocument;
import com.kartishan.bookscroll.model.newBookMark.UserBookMark;
import com.kartishan.bookscroll.service.jwt.JwtService;
import com.kartishan.bookscroll.service.jwt.UserService;
import com.kartishan.bookscroll.service.newBookMark.UserBookMarkService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/new/bookmarks")
@RequiredArgsConstructor
public class NewBookMarkController {
    private final UserBookMarkService userBookMarkService;
    private final JwtService jwtService;
    private final UserService userService;

    @PostMapping("/{bookId}")
    public ResponseEntity<Boolean > createBookmark(@RequestBody NewBookmarkDTO newBookmarkDTO, @PathVariable UUID bookId, HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        UUID userId = null;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            String username = jwtService.extractUsername(token);
            User user = userService.getUserByUsername(username);
            if (user != null) {
                userId = user.getId();
            }
        }

        if (userId == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User must be authenticated");
        }
        System.out.println("alloha");
        userBookMarkService.addOrUpdateBookmark(userId, bookId, newBookmarkDTO);
        return new ResponseEntity<>( HttpStatus.CREATED);
    }

    @GetMapping("/{bookId}")
    public ResponseEntity<List<BookmarkDocument.Bookmark>> getAllBookmarks(@PathVariable UUID bookId, HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        UUID userId = null;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            String username = jwtService.extractUsername(token);
            User user = userService.getUserByUsername(username);
            if (user != null) {
                userId = user.getId();
            }
        }

        if (userId == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User must be authenticated");
        }
        List<BookmarkDocument.Bookmark> bookmarks = userBookMarkService.getBookmarks(userId, bookId);
        return new ResponseEntity<>(bookmarks, HttpStatus.OK);
    }

    @DeleteMapping("/{bookId}/{markId}")
    public ResponseEntity<Void> deleteBookmark(@PathVariable UUID bookId,@PathVariable UUID markId,
                                               HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        UUID userId = null;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            String username = jwtService.extractUsername(token);
            User user = userService.getUserByUsername(username);
            if (user != null) {
                userId = user.getId();
            }
        }

        if (userId == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User must be authenticated");
        }
        try {
            userBookMarkService.removeBookmark(userId, bookId,markId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/share/{bookId}")
    public ResponseEntity<?> shareBookmarks(@PathVariable UUID bookId, HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        UUID userId = null;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            String username = jwtService.extractUsername(token);
            User user = userService.getUserByUsername(username);
            if (user != null) {
                userId = user.getId();
            }
        }

        if (userId == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User must be authenticated");
        }
        try {
            UUID token = userBookMarkService.shareBookmarks(userId, bookId);
            return ResponseEntity.ok().body(token);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping("/import/{tokenBookMarks}")
    public ResponseEntity<?> importBookmarks(@PathVariable UUID tokenBookMarks,
                                             HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        UUID userId = null;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            String username = jwtService.extractUsername(token);
            User user = userService.getUserByUsername(username);
            if (user != null) {
                userId = user.getId();
            }
        }

        if (userId == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User must be authenticated");
        }
        try {
            userBookMarkService.importBookmarks(userId, tokenBookMarks);
            return ResponseEntity.ok().body("Bookmarks imported successfully.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
