package com.example.demo.dto;

public record ClienteDTO(
        Long id,
        String nombre,
        int edad,
        String genero,
        boolean intolerancia
) {}

