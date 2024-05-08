package com.kartishan.bookscroll.controller;

import com.kartishan.bookscroll.model.LastPlaybackPosition;
import com.kartishan.bookscroll.model.User;
import com.kartishan.bookscroll.model.dto.LastPlaybackPositionDTO;
import com.kartishan.bookscroll.request.PlaybackPositionUpdateRequest;
import com.kartishan.bookscroll.service.LastPlaybackPositionService;
import com.kartishan.bookscroll.service.jwt.JwtService;
import com.kartishan.bookscroll.service.jwt.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/playback")
@RequiredArgsConstructor
public class LastPlaybackPositionController {
    private final LastPlaybackPositionService lastPlaybackPositionService;
    private final JwtService jwtService;
    private final UserService userService;

    @PostMapping
    public ResponseEntity<?> updatePlaybackPosition(@RequestBody PlaybackPositionUpdateRequest playbackRequest, HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        UUID userId = null;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            String username = jwtService.extractUsername(token);

            User user = userService.getUserByUsername(username);
            if (user != null) {
                userId = user.getId();
            }
        }
        LastPlaybackPosition playbackPosition = lastPlaybackPositionService.updatePlaybackPosition(
                userId,
                playbackRequest.getBookId(),
                playbackRequest.getChapterNumber(),
                playbackRequest.getLastPlaybackPosition()
        );

        if (playbackPosition != null) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().body("Не удалось обновить позицию воспроизведения");
        }
    }

    @GetMapping("/{bookId}")
    public ResponseEntity<LastPlaybackPositionDTO> getPlaybackPosition(@PathVariable UUID bookId, HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        UUID userId = null;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            String username = jwtService.extractUsername(token);

            User user = userService.getUserByUsername(username);
            if (user != null) {
                userId = user.getId();
            }
        }
        LastPlaybackPosition playbackPosition = lastPlaybackPositionService.getPlaybackPosition(userId, bookId);

        LastPlaybackPositionDTO responseDTO = LastPlaybackPositionDTO.builder()
                .chapterId(playbackPosition.getChapter().getId())
                .lastPlaybackPosition(playbackPosition.getLastPlaybackPosition())
                .build();

        return ResponseEntity.ok(responseDTO);
    }
    @GetMapping("/last")
    public ResponseEntity<LastPlaybackPosition> getLastPlaybackPosition(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        UUID userId = null;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            String username = jwtService.extractUsername(token);

            User user = userService.getUserByUsername(username);
            if (user != null) {
                userId = user.getId();
            }
        }


        LastPlaybackPosition playbackPosition = lastPlaybackPositionService.getLastPlaybackPositionForUser(userId);

        return ResponseEntity.ok(playbackPosition);
    }
}
