package com.kartishan.bookscroll.controller;

import com.kartishan.bookscroll.request.UpdateUserBookPreferenceRequest;
import com.kartishan.bookscroll.service.UserBookPreferenceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/preference")
@RequiredArgsConstructor
public class UserBookPreferenceController {
    private final UserBookPreferenceService userBookPreferenceService;

    @PutMapping("/update")
    public ResponseEntity<String> updateUserBookPreference(@RequestBody UpdateUserBookPreferenceRequest request) {
        try {
            userBookPreferenceService.updateUserBookPreference(request.getUserId(), request.getBookId(), request.getRating());
            return ResponseEntity.ok("User book preference updated successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update user book preference.");
        }
    }
}
