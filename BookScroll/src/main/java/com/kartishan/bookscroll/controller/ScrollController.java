package com.kartishan.bookscroll.controller;


import com.kartishan.bookscroll.model.Scroll;
import com.kartishan.bookscroll.model.User;
import com.kartishan.bookscroll.model.dto.ScrollDTO;
import com.kartishan.bookscroll.request.ScrollRequest;
import com.kartishan.bookscroll.service.ScrollRecommendationService;
import com.kartishan.bookscroll.service.ScrollService;
import com.kartishan.bookscroll.service.jwt.JwtService;
import com.kartishan.bookscroll.service.jwt.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/scroll")
@RequiredArgsConstructor
public class ScrollController {
    private final ScrollService scrollService;
    private final JwtService jwtService;
    private final UserService userService;
    private final ScrollRecommendationService scrollRecommendationService;

    @PostMapping("/create")
    public ResponseEntity<?> createScroll(@RequestBody ScrollRequest scrollRequest,HttpServletRequest request){
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

        if (userId == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User must be authenticated");
        }
        scrollService.createScroll(userId, scrollRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body("Scroll успешно создан");
    }



    @GetMapping("/{id}")
    public ResponseEntity<ScrollDTO> getScroll(@PathVariable UUID id, HttpServletRequest request){
        Scroll scroll = scrollService.getScroll(id);
        ScrollDTO scrollDTO = scrollService.getScrollDTO(scroll);
        addScrollViewAndHistory(scroll, request);
        return ResponseEntity.ok().body(scrollDTO);
    }

    private void addScrollViewAndHistory(Scroll scroll, HttpServletRequest request) {
        try {
            scrollService.addScrollView(scroll);

            String authorizationHeader = request.getHeader("Authorization");
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                String token = authorizationHeader.substring(7);
                String username = jwtService.extractUsername(token);

                User user = userService.getUserByUsername(username);
                if (user != null) {
                    scrollService.addScrollViewHistory(user, scroll);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    @GetMapping("/recommendations/")
    public ResponseEntity<List<ScrollDTO>> getPersonalizedScrolls(
            @RequestParam(defaultValue = "10") int limit,
            HttpServletRequest request) {

        String authorizationHeader = request.getHeader("Authorization");
        List<ScrollDTO> personalizedScrolls=null;
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            String username = jwtService.extractUsername(token);

            User user = userService.getUserByUsername(username);
            if (user != null) {
                personalizedScrolls = scrollRecommendationService.getPersonalizedScrolls(user.getId(), limit);
            }
        }

//        List<ScrollDTO> personalizedScrolls = scrollRecommendationService.getPersonalizedScrolls(userId, limit);

        if (personalizedScrolls.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(personalizedScrolls);
    }
    @PutMapping("/like/{scrollId}")
    public ResponseEntity<?> likeScroll(@PathVariable UUID scrollId, HttpServletRequest request) {
        try {
            String authorizationHeader = request.getHeader("Authorization");
            if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
            String token = authorizationHeader.substring(7);
            String username = jwtService.extractUsername(token);
            User user = userService.getUserByUsername(username);
            scrollService.likeScroll(user.getId(), scrollId);

            return ResponseEntity.noContent().build();
        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating book information: ");
        }
    }
    @GetMapping("/{scrollId}/like-status")
    public ResponseEntity<Boolean> userLike(@PathVariable UUID scrollId, HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String token = authorizationHeader.substring(7);
        String username = jwtService.extractUsername(token);

        User user = userService.getUserByUsername(username);

        scrollService.userLike(scrollId,user.getId());

        return ResponseEntity.ok().body(scrollService.userLike(scrollId,user.getId()));
    }
}
