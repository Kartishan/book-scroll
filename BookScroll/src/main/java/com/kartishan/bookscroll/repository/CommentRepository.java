package com.kartishan.bookscroll.repository;

import com.kartishan.bookscroll.model.Book;
import com.kartishan.bookscroll.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CommentRepository extends JpaRepository<Comment, UUID> {
    Optional<Comment> findById(UUID id);
    List<Comment> findAllCommentsByParentComment(Comment parentComment);
    List<Comment> findByBookId(UUID bookId);
}