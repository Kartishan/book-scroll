package com.kartishan.bookscroll.repository;


import com.kartishan.bookscroll.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ImageRepository extends JpaRepository<Image, UUID> {
    Image findByBookId(UUID bookId);
}
