package com.kartishan.bookscroll.controller;


import com.kartishan.bookscroll.model.User;
import com.kartishan.bookscroll.request.AuthenticationRequest;
import com.kartishan.bookscroll.request.RegisterRequest;
import com.kartishan.bookscroll.response.AuthenticationResponse;
import com.kartishan.bookscroll.response.AuthenticationTokenResponse;
import com.kartishan.bookscroll.service.jwt.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.security.PermitAll;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Tag(name = "Authentication methods api")
@CrossOrigin(origins = "http://localhost:3000/")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService service;
    @Operation(
            summary = "Метод для регистрации пользователя",
            description = "Для регистрации нужен логин, почта, пароль"
    )
    @PostMapping("/signup")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody RegisterRequest request, HttpServletResponse response) {
        AuthenticationResponse authResponse = service.register(request);

        Cookie refreshTokenCookie = new Cookie("refresh_token", authResponse.getRefreshToken());
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setPath("/");

        response.addCookie(refreshTokenCookie);

        return ResponseEntity.ok(new AuthenticationResponse(authResponse.getAccessToken(), null));
    }
    @Operation(
            summary = "Метод для входа пользователя",
            description = "Для входа нужна почта, пароль"
    )
    @PostMapping("/signin")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request, HttpServletResponse response) {
        AuthenticationResponse authResponse = service.authenticate(request);

        Cookie refreshTokenCookie = new Cookie("refresh_token", authResponse.getRefreshToken());
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setPath("/");

        response.addCookie(refreshTokenCookie);

        return ResponseEntity.ok(new AuthenticationResponse(authResponse.getAccessToken(), null) );
    }
    @Operation(
            summary = "Метод для обновления access токена",
            description = "Для обновления токена нужен refresh токен"
    )
    @PostMapping("/refresh-token")
    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        service.refreshToken(request, response);
    }
    @Operation(
            summary = "Метод для аутентификации пользователя из токена",
            description = "Аутентификация пользователя из токена."
    )
    @GetMapping("/authenticate-from-token")
    public ResponseEntity<User> authenticateFromToken(HttpServletRequest request) {
        User authenticatedUser = service.authenticateFromToken(request);
        if (authenticatedUser != null) {
            return ResponseEntity.ok(authenticatedUser);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
