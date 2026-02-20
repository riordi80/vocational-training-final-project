package com.example.proyectoarboles.dto;

/**
 * DTO para solicitud de registro de nuevo usuario
 * Se envía al backend en POST /api/auth/register
 */
public class RegisterRequest {
    private String nombre;
    private String email;
    private String password;

    public RegisterRequest(String nombre, String email, String password) {
        this.nombre = nombre;
        this.email = email;
        this.password = password;
    }

    // Getters
    public String getNombre() {
        return nombre;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    // Setters
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
