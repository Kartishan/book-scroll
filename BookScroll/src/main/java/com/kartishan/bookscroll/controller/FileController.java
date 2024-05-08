package com.kartishan.bookscroll.controller;

import com.kartishan.bookscroll.service.AudioService;
import com.kartishan.bookscroll.service.GridFsService;
import com.kartishan.bookscroll.service.ManagerBookService;
import com.mongodb.client.gridfs.model.GridFSFile;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
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
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileId) {
        GridFSFile gridFSFile = gridFsService.retrieveFile(fileId);
        if (gridFSFile == null) {
            return ResponseEntity.notFound().build();
        }

        String fileName = gridFSFile.getFilename();
        String contentType = Optional.ofNullable(gridFSFile.getMetadata().get("_contentType").toString())
                .orElse("application/octet-stream");

        InputStreamResource resource = gridFsService.downloadFileAsResource(fileId);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.ACCEPT_RANGES, "bytes")
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + fileName + "\"")
                .contentLength(gridFSFile.getLength())
                .body(resource);
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
