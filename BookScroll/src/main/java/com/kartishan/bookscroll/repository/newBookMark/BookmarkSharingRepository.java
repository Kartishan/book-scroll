package com.kartishan.bookscroll.repository.newBookMark;

import com.kartishan.bookscroll.model.newBookMark.BookmarkSharing;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BookmarkSharingRepository extends JpaRepository<BookmarkSharing, UUID> {
}
