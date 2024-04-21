package com.kartishan.bookscroll.controller;

import com.kartishan.bookscroll.model.Comment;
import com.kartishan.bookscroll.request.CommentRequest;
import com.kartishan.bookscroll.service.CommentService;
import io.swagger.v3.oas.annotations.tags.Tag;
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

    @GetMapping("/{id}")
    public ResponseEntity<List<Comment>> getCoomentsForParentComment(@PathVariable("id")UUID id){
        List<Comment> comments = commentService.getCommentsForParentComment(id);
        return ResponseEntity.ok(comments);
    }
    @GetMapping("book/{id}")
    public ResponseEntity<List<Comment>> getCom(@PathVariable("id") UUID id){
        List<Comment> comments = commentService.getCoommentsForBook(id);
        return ResponseEntity.ok(comments);
    }
    @PostMapping("/add")
    public ResponseEntity<?> addComment(@RequestBody CommentRequest request){
        commentService.createNewComment(request);
        return ResponseEntity.status(HttpStatus.CREATED).body("Комментарий добавлен успешно.");
    }
}
