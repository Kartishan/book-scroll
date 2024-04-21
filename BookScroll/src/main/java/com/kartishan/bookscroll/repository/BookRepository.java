package com.kartishan.bookscroll.repository;


import com.kartishan.bookscroll.model.Book;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;



import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BookRepository extends JpaRepository<Book, UUID> {
    Optional<Book> findById(UUID id);
    Optional<Book> findByName(String name);
    Page<Book> findByNameContaining(String name, Pageable pageable);


    @Query("SELECT b FROM Book b JOIN b.categories c WHERE c.name = :categoryName")
    Page<Book> findByCategory(@Param("categoryName") String categoryName, Pageable pageable);
}
