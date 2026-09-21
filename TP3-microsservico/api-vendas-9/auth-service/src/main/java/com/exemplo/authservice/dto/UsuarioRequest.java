package com.exemplo.authservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioRequest {

    @NotBlank(message = "nome e obrigatorio")
    private String nome;

    @NotBlank(message = "email e obrigatorio")
    @Email(message = "email invalido")
    private String email;

    @NotBlank(message = "senha e obrigatoria")
    @Size(min = 6, message = "senha deve ter no minimo 6 caracteres")
    private String senha;
}
