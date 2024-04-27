package com.kartishan.bookscroll.controller;

import com.kartishan.bookscroll.model.User;
import com.kartishan.bookscroll.request.UpdateUserBookPreferenceRequest;
import com.kartishan.bookscroll.service.UserBookPreferenceService;
import com.kartishan.bookscroll.service.jwt.JwtService;
import com.kartishan.bookscroll.service.jwt.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/preference")
@RequiredArgsConstructor
public class UserBookPreferenceController {
    private final UserBookPreferenceService userBookPreferenceService;
    private final JwtService jwtService;
    private final UserService userService;

    @PostMapping("/update")
    public ResponseEntity<String> updateUserBookPreference(@RequestBody UpdateUserBookPreferenceRequest RatingRequest, HttpServletRequest request) {
        try {
            System.out.println("fsd");
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
            System.out.println(userId);
            userBookPreferenceService.updateUserBookPreference(userId, RatingRequest.getBookId(), RatingRequest.getRating());
            return ResponseEntity.ok("User book preference updated successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update user book preference.");
        }
    }
}
