package com.kartishan.bookscroll.controller;

import com.kartishan.bookscroll.model.Comment;
import com.kartishan.bookscroll.model.User;
import com.kartishan.bookscroll.model.dto.CommentDTO;
import com.kartishan.bookscroll.request.CommentRequest;
import com.kartishan.bookscroll.service.CommentService;
import com.kartishan.bookscroll.service.jwt.JwtService;
import com.kartishan.bookscroll.service.jwt.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@Tag(name="Comment methods api")
    @CrossOrigin(origins = "http://localhost:3000")
    @RestController
    @RequestMapping("/api/comments/")
    @RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;
    private final JwtService jwtService;
    private final UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<List<Comment>> getCommentsForParentComment(@PathVariable("id")UUID id){
        List<Comment> comments = commentService.getCommentsForParentComment(id);
        return ResponseEntity.ok(comments);
    }
    @GetMapping("book/{id}")
    public ResponseEntity<List<CommentDTO>> getCommentsForBook(@PathVariable("id") UUID id){
        List<CommentDTO> comments = commentService.getAllCommentsForBookDTO(id);
        return ResponseEntity.ok(comments);
    }
    @PostMapping("/add")
    public ResponseEntity<?> addComment(@RequestBody CommentRequest commentRequest, HttpServletRequest request){
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
        commentService.createNewComment(commentRequest, UserId);
        return ResponseEntity.status(HttpStatus.CREATED).body("Комментарий добавлен успешно.");
    }
}
