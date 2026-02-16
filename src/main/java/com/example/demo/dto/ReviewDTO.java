package com.example.demo.dto;

public record ReviewDTO(
        Long id,
        String descripcion,
        int valoracion,
        Long clienteId
) {}

