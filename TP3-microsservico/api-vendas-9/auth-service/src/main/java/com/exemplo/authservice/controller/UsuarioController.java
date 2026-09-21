package com.exemplo.authservice.controller;

import com.exemplo.authservice.dto.LoginRequest;
import com.exemplo.authservice.dto.LoginResponse;
import com.exemplo.authservice.dto.RefreshRequest;
import com.exemplo.authservice.dto.RefreshResponse;
import com.exemplo.authservice.dto.UsuarioRequest;
import com.exemplo.authservice.model.Usuario;
import com.exemplo.authservice.service.JwtToken;
import com.exemplo.authservice.service.UsuarioService;
import io.jsonwebtoken.JwtException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService service;
    private final JwtToken jwtService;

    @PostMapping
    public ResponseEntity<Long> cadastrar(@Valid @RequestBody UsuarioRequest request) {
        Usuario usuario = service.cadastrar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(usuario.getId());
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        Usuario usuario = this.service.autenticar(request);
        String token = jwtService.gerarToken(usuario);
        String refreshToken = jwtService.gerarRefreshToken(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(new LoginResponse(token, refreshToken));
    }

    @PostMapping("/refresh")
    public ResponseEntity<RefreshResponse> refresh(@RequestBody RefreshRequest request) {
        try {
            String email = jwtService.validarRefreshToken(request.getRefreshToken());
            Usuario usuario = service.buscarPorEmail(email);
            String novoToken = jwtService.gerarToken(usuario);
            return ResponseEntity.ok(new RefreshResponse(novoToken));
        } catch (JwtException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }
}