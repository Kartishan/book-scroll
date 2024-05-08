//package com.kartishan.bookscroll.controller;
//
//
//import com.kartishan.bookscroll.model.Bookmark;
//import com.kartishan.bookscroll.model.User;
//import com.kartishan.bookscroll.request.BookmarkCreateRequest;
//import com.kartishan.bookscroll.service.BookmarkService;
//import com.kartishan.bookscroll.service.jwt.JwtService;
//import com.kartishan.bookscroll.service.jwt.UserService;
//import jakarta.servlet.http.HttpServletRequest;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.HttpStatus;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.server.ResponseStatusException;
//
//import java.util.List;
//import java.util.UUID;
//
//@CrossOrigin(origins = "http://localhost:3000")
//@RestController
//@RequestMapping("/api/bookmarks")
//@RequiredArgsConstructor
//public class BookmarkController {
//    private final BookmarkService bookmarkService;
//    private final JwtService jwtService;
//    private final UserService userService;
//
//    @GetMapping("/{bookId}")
//    public List<Bookmark> getBookmarksByUserAndBook(
//            @PathVariable UUID bookId,
//            HttpServletRequest request) {
//        String authorizationHeader = request.getHeader("Authorization");
//        UUID userId = null;
//
//        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
//            String token = authorizationHeader.substring(7);
//            String username = jwtService.extractUsername(token);
//
//            User user = userService.getUserByUsername(username);
//            if (user != null) {
//                userId = user.getId();
//            }
//        }
//        System.out.println(userId);
//        System.out.println(bookId);
//        return bookmarkService.getBookmarksByUserAndBook(userId, bookId);
//    }
//
//    @PostMapping
//    public Bookmark createBookmark(@RequestBody BookmarkCreateRequest createRequest, HttpServletRequest request) {
//        String authorizationHeader = request.getHeader("Authorization");
//        UUID userId = null;
//
//        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
//            String token = authorizationHeader.substring(7);
//            String username = jwtService.extractUsername(token);
//            User user = userService.getUserByUsername(username);
//            if (user != null) {
//                userId = user.getId();
//            }
//        }
//
//        if (userId == null) {
//            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User must be authenticated");
//        }
//
//        return bookmarkService.saveBookmark(userId, createRequest.getBookId(), createRequest.getCfiRange(), createRequest.getText(), createRequest.getComment());
//    }
//
//    @DeleteMapping("/{bookmarkId}")
//    public void deleteBookmark(@PathVariable UUID bookmarkId) {
//        bookmarkService.deleteBookmark(bookmarkId);
//    }
//
//
//
//}
