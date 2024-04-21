package com.kartishan.bookscroll.service;

import com.kartishan.bookscroll.model.Book;
import com.kartishan.bookscroll.model.Category;
import com.kartishan.bookscroll.repository.BookRepository;
import com.kartishan.bookscroll.repository.CategoryRepository;
import com.kartishan.bookscroll.request.BookRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class ManagerBookService {
    private final BookRepository bookRepository;
    private final CategoryRepository categoryRepository;
    private final GridFsService gridFsService;

    public boolean uploadBookFile(UUID bookId, MultipartFile file) {
        Optional<Book> bookOptional = bookRepository.findById(bookId);

        if (!bookOptional.isPresent()) {
            log.error("Book with ID: " + bookId + " not found.");
            return false;
        }
        Book book = bookOptional.get();
        if (book.getFileId() != null) {
            log.error("A file is already associated with the book with ID: " + bookId);
            return false;
        }
        try {
            String fileId = gridFsService.storeFile(file);
            book.setFileId(fileId);
            bookRepository.save(book);
            log.info("File successfully uploaded and associated with book with ID: " + bookId);
            return true;
        } catch (IOException e) {
            log.error("Failed to upload file: " + e.getMessage(), e);
            return false;
        }
    }

    public void createBook(BookRequest bookRequest) {
        System.out.println(bookRequest);
        try {
            Book book = new Book();
            book.setName(bookRequest.getName());
            book.setAuthor(bookRequest.getAuthor());
            book.setAuthorFull(bookRequest.getAuthorFullName());
            book.setDescription(bookRequest.getDescription());
            book.setPageCount(bookRequest.getPageCount());
            book.setRating(0);

            Set<Category> categories = new HashSet<>();
            for (String categoryName : bookRequest.getCategories()) {
                Category category = categoryRepository.findByName(categoryName)
                        .orElseGet(() -> {
                            Category newCategory = new Category();
                            newCategory.setName(categoryName);
                            return categoryRepository.save(newCategory);
                        });
                categories.add(category);
            }
            book.setCategories(categories);
            bookRepository.save(book);
        } catch (Exception e) {
            log.error("Error creating book: " + e.getMessage(), e);
        }
    }



    public void changeBookInformation(Book book){
        try{
            UUID id = book.getId();
            Optional<Book> newBook = bookRepository.findById(id);
            newBook = Optional.of(book);
            bookRepository.save(newBook.get());
        }catch (Exception e){
            log.error("Error changing book: " + e);
        }

    }

}
