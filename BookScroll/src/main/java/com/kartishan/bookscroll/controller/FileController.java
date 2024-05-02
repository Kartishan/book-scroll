package com.kartishan.bookscroll.controller;

import com.kartishan.bookscroll.service.AudioService;
import com.kartishan.bookscroll.service.GridFsService;
import com.kartishan.bookscroll.service.ManagerBookService;
import com.mongodb.client.gridfs.model.GridFSFile;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;
import java.util.UUID;


@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/file")
@RequiredArgsConstructor
public class FileController {
    private final GridFsService gridFsService;
    private final ManagerBookService managerBookService;
    private final AudioService audioService;

    @GetMapping("/{fileId}")
    public ResponseEntity<InputStreamResource> downloadFile(@PathVariable String fileId) {
        InputStreamResource inputStreamResource = gridFsService.downloadFileAsResource(fileId);

        GridFSFile gridFSFile = gridFsService.retrieveFile(fileId);
        String fileName = gridFSFile.getFilename();

        String contentType = Optional.ofNullable(gridFSFile.getMetadata().get("_contentType").toString())
                .orElse("application/octet-stream");

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .contentLength(gridFSFile.getLength())
                .body(inputStreamResource);
    }
    @PostMapping("/book/upload/{bookId}")
    public ResponseEntity<String> uploadBookFile(@PathVariable UUID bookId, @RequestParam("file") MultipartFile file) {
        boolean uploadSuccess = managerBookService.uploadBookFile(bookId, file);
        if (uploadSuccess) {
            return ResponseEntity.status(HttpStatus.OK).body("Файл успешно загружен для книги с ID: " + bookId);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Произошла ошибка при загрузке файла книги с ID: " + bookId);
        }
    }
    @PostMapping("/audio/upload/{bookId}/{chapterNumber}")
    public ResponseEntity<String> uploadAudioChapter(@PathVariable UUID bookId,
                                                     @PathVariable Long chapterNumber,
                                                     @RequestParam("file") MultipartFile file) {
        boolean uploadSuccess = audioService.uploadAudioChapter(bookId, chapterNumber, file);
        if (uploadSuccess) {
            return ResponseEntity.status(HttpStatus.OK).body("Аудиофайл успешно загружен для главы " + chapterNumber + " книги с ID: " + bookId);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Произошла ошибка при загрузке аудиофайла для главы " + chapterNumber + " книги с ID: " + bookId);
        }
    }
}
