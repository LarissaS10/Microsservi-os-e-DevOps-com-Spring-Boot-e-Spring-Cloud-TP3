package com.exemplo.authservice.service;

import java.util.Date;
import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.exemplo.authservice.model.Usuario;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtToken {

    private final SecretKey secretKey;
    private final long accessExpirationMs;
    private final long refreshExpirationMs;

    public JwtToken(
            @Value("${jwt.secret}") String segredo,
            @Value("${jwt.access-expiration-ms}") long accessExpirationMs,
            @Value("${jwt.refresh-expiration-ms}") long refreshExpirationMs) {
        this.secretKey = Keys.hmacShaKeyFor(segredo.getBytes());
        this.accessExpirationMs = accessExpirationMs;
        this.refreshExpirationMs = refreshExpirationMs;
    }

    //token de acesso: vida curta, usado nas requisicoes normais
    public String gerarToken(Usuario usuario) {
        Date agora = new Date();
        return Jwts.builder()
                .subject(usuario.getEmail())
                .claim("type", "access")
                .issuedAt(agora)
                .expiration(new Date(agora.getTime() + accessExpirationMs))
                .signWith(secretKey)
                .compact();
    }

    //refresh token: vida longa, serve so para pedir um novo token de acesso
    public String gerarRefreshToken(Usuario usuario) {
        Date agora = new Date();
        return Jwts.builder()
                .subject(usuario.getEmail())
                .claim("type", "refresh")
                .issuedAt(agora)
                .expiration(new Date(agora.getTime() + refreshExpirationMs))
                .signWith(secretKey)
                .compact();
    }

    public String validarRefreshToken(String token) {
        Claims claims;
        try {
            claims = Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (ExpiredJwtException e) {
            throw new JwtException("Refresh token expirado");
        } catch (Exception e) {
            throw new JwtException("Refresh token invalido");
        }

        if (!"refresh".equals(claims.get("type", String.class))) {
            throw new JwtException("Token informado nao e um refresh token");
        }

        return claims.getSubject();
    }
}