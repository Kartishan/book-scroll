package com.kartishan.bookscroll.service;



import com.kartishan.bookscroll.exceptions.BookNotFoundException;
import com.kartishan.bookscroll.exceptions.ImageNotFoundException;
import com.kartishan.bookscroll.model.Book;
import com.kartishan.bookscroll.model.Image;
import com.kartishan.bookscroll.repository.ImageRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class ImageService {
    private final ImageRepository imageRepository;
    private final BookService bookService;

    public Image getImageByBookId(UUID id){
        try {
            Image image = imageRepository.findByBookId(id);
            return image;

        }catch (Exception e){
            throw new ImageNotFoundException(e.toString());
        }

    }

    @Transactional
    public void saveImageForBook(UUID bookId, MultipartFile file) throws IOException {
        Book book = bookService.getBookById(bookId);
        if (book == null) {
            throw new BookNotFoundException("Book with ID " + bookId + " not found");
        }

        Image image = toImageEntity(file);
        image.setBook(book);
        imageRepository.save(image);
    }

    private Image toImageEntity(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("MultipartFile must not be null or empty");
        }
        Image image = new Image();
        image.setName(file.getName());
        image.setOriginalFileName(file.getOriginalFilename());
        image.setContentType(file.getContentType());
        image.setSize(file.getSize());
        try {
            image.setBytes(file.getBytes());
        } catch (IOException e) {
            throw new IOException("Failed to read bytes from MultipartFile", e);
        }
        return image;
    }
}
