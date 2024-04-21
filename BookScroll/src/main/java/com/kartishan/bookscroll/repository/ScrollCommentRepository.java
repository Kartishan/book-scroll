package com.kartishan.bookscroll.repository;

import com.kartishan.bookscroll.model.ScrollComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ScrollCommentRepository extends JpaRepository<ScrollComment, UUID> {
    Optional<ScrollComment> findById(UUID id);
    List<ScrollComment> findByScrollId(UUID scrollId);
    List<ScrollComment> findByParentComment(ScrollComment parent);
}
