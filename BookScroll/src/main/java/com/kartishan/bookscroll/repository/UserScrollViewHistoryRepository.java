package com.kartishan.bookscroll.repository;

import com.kartishan.bookscroll.model.UserScrollViewHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface UserScrollViewHistoryRepository extends JpaRepository<UserScrollViewHistory, UUID> {

    List<UserScrollViewHistory> findAllByScroll_Id(UUID scrollId);

    List<UserScrollViewHistory> findAllByUser_Id(UUID userId);
}
