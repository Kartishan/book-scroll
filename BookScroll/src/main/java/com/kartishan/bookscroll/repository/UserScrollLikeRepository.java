package com.kartishan.bookscroll.repository;

import com.kartishan.bookscroll.model.Scroll;
import com.kartishan.bookscroll.model.User;
import com.kartishan.bookscroll.model.UserScrollLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserScrollLikeRepository extends JpaRepository<UserScrollLike, UUID> {
    Optional<UserScrollLike> findByScrollId(UUID id);

    List<UserScrollLike> findAllByScroll_Id(UUID scrollId);

    List<UserScrollLike> findAllByUserId(UUID userId);

    Optional<UserScrollLike> findByUserAndScroll(User user, Scroll scroll);

    Optional<UserScrollLike> findByUserIdAndScrollId(UUID userId, UUID scrollId);
}
