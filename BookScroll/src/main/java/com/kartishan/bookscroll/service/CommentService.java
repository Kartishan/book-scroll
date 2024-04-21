package com.kartishan.bookscroll.service;

import com.kartishan.bookscroll.exceptions.CommentNotFoundException;
import com.kartishan.bookscroll.model.Book;
import com.kartishan.bookscroll.model.Comment;
import com.kartishan.bookscroll.repository.CommentRepository;
import com.kartishan.bookscroll.request.CommentRequest;
import com.kartishan.bookscroll.service.jwt.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final BookService bookService;
    private final UserService userService;

    public Comment getCommentById(UUID id){
        try {
            return commentRepository.findById(id).get();
        }catch (Exception e){
            throw new CommentNotFoundException("Комментрий с id: " + id + " не найден");
        }
    }
    public List<Comment> getCoommentsForBook(UUID id){
        Book book = bookService.getBookById(id);
        return (commentRepository.findByBook(book));
    }
    public List<Comment> getCommentsForParentComment(UUID id){
        Optional<Comment> commentOptional = commentRepository.findById(id);
        if (commentOptional.isPresent()) {
            Comment comment = commentOptional.get();
            return (commentRepository.findAllCommentsByParentComment(comment));
        } else {
            throw new RuntimeException("Ошибка: Комментарий с указанным ID не найден.");
        }
    }
    public void createNewComment(CommentRequest commentRequest){
        Comment comment = new Comment();
        comment.setBook(bookService.getBookById(commentRequest.getBookId()));
        if (commentRequest.getParentCommentId()!=null){
            comment.setParentComment(getCommentById(commentRequest.getBookId()));
        }
        comment.setUser(userService.getUserById(commentRequest.getUserId()));
        comment.setTitle(commentRequest.getTittle());
        commentRepository.save(comment);
    }

}
