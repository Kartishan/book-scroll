package com.kartishan.bookscroll.controller;



import com.kartishan.bookscroll.model.Image;
import com.kartishan.bookscroll.service.ImageService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Tag(name = "Image methods api")
@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/image")
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;

    @PostMapping("/upload/{bookId}")
    public ResponseEntity<String> saveImageForBook(@PathVariable UUID bookId,
                                                   @RequestParam("file") MultipartFile file) {
        try {
            imageService.saveImageForBook(bookId, file);
            return ResponseEntity.status(HttpStatus.CREATED).body("Image saved successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getMessage());
        }
    }

    @GetMapping("/{bookId}")
    @Transactional(readOnly = true)
    public ResponseEntity<Resource> getImageByBookId(@PathVariable UUID bookId) {
        Image image = imageService.getImageByBookId(bookId);

        ByteArrayResource resource = new ByteArrayResource(image.getBytes());

        return ResponseEntity.ok()
                .contentType(MediaType.valueOf(image.getContentType()))
                .contentLength(image.getSize())
                .body(resource);
    }


}
