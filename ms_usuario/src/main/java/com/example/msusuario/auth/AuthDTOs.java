package com.example.msusuario.auth;

import com.example.msusuario.DTO.UsuarioDTO;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class AuthDTOs {

    public record LoginRequest(
            @NotBlank(message = "El correo es obligatorio") @Email(message = "Correo inválido") String correo,
            @NotBlank(message = "La contraseña es obligatoria") String password) {
    }

    public record RegistroRequest(
            @NotBlank(message = "El nombre es obligatorio") @Size(min = 3, max = 100) String nombre,
            @NotBlank(message = "El correo es obligatorio") @Email(message = "Correo inválido") String correo,
            @NotBlank(message = "La contraseña es obligatoria") @Size(min = 6, message = "Mínimo 6 caracteres") String password) {
    }

    public record AuthResponse(String token, UsuarioDTO usuario) {
    }
}
