package com.exemple.auth.security;

import com.exemple.auth.security.dtos.JwtAuthDto;
import com.exemple.auth.security.dtos.RefreshTokenDto;
import com.exemple.auth.security.dtos.UserCredentialsDto;
import com.exemple.auth.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.naming.AuthenticationException;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;

    @RequestMapping("/sign-in")
    public ResponseEntity<JwtAuthDto> signIn(@RequestBody UserCredentialsDto credentialsDto) {
        try {
            JwtAuthDto jwtAuthDto = userService.singIn(credentialsDto);
            return ResponseEntity.ok(jwtAuthDto);
        } catch (AuthenticationException ex) {
            throw new RuntimeException("Authentification failed" + ex);
        }
    }

    @PostMapping("/refresh")
    public JwtAuthDto refresh(@RequestBody RefreshTokenDto refreshTokenDto) throws Exception {
        return userService.refreshToken(refreshTokenDto);
    }
}
