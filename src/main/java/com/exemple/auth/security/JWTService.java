package com.exemple.auth.security;

import com.exemple.auth.security.dtos.JwtAuthDto;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import javax.crypto.SecretKey;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Component
public class JWTService {
    private static final Logger LOGGER = LogManager.getLogger(JWTService.class);
    @Value("${JWT_SECRET}")
    private String jwtSecret;

    public JwtAuthDto generateAuthToken(String username) {
        JwtAuthDto jwtDto = new JwtAuthDto();
        jwtDto.setToken(generateJwtToken(username));
        jwtDto.setRefreshToken(generateRefreshToken(username));
        return  jwtDto;

    }

    public String generateJwtToken(String username) {
        Date date = Date.from(LocalDateTime
                .now()
                .plusHours(1)
                .atZone(ZoneId.systemDefault())
                .toInstant());

        return Jwts.builder()
                .subject(username)
                .expiration(date)
                .signWith(getSingInKey())
                .compact();
    }

    public JwtAuthDto refreshBaseToken(String username, String refreshToken) {
        JwtAuthDto jwtDto = new JwtAuthDto();
        jwtDto.setToken(generateJwtToken(username));
        jwtDto.setRefreshToken(generateRefreshToken(username));
        return  jwtDto;
    }

    public String getUserNameFromToken(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(getSingInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return  claims.getSubject();
    }

    public boolean validateJwtToken(String token) {
        try{
            Jwts.parser()
                    .verifyWith(getSingInKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            return true;
        } catch (ExpiredJwtException exp) {
            LOGGER.error("Expired JwtException", exp);
        } catch (UnsupportedJwtException exp) {
            LOGGER.error("Unsupported JwtException", exp);
        } catch (SecurityException exp) {
            LOGGER.error("Security Exception", exp);
        } catch (Exception exp) {
            LOGGER.error("Invalid token", exp);
        }
        return  false;
    }

    public String generateRefreshToken(String username) {
        Date date = Date.from(LocalDateTime
                .now()
                .plusDays(1)
                .atZone(ZoneId.systemDefault())
                .toInstant());

        return Jwts.builder()
                .subject(username)
                .expiration(date)
                .signWith(getSingInKey())
                .compact();
    }

    private SecretKey getSingInKey() {
        byte [] keyBytes = Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}

