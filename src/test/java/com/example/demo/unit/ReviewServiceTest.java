package com.example.demo.unit;

import com.example.demo.entity.Review;
import com.example.demo.repository.ReviewRepository;
import com.example.demo.service.ReviewService; // O ReviewServiceImpl si es interfaz
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {

    @Mock
    private ReviewRepository reviewRepository;

    @InjectMocks
    private ReviewService reviewService; 


    @Test
    void testGuardarReview() {
        // 1. Datos de prueba
        Review reviewInput = new Review();
        reviewInput.setDescripcion("Muy buena atención");
        reviewInput.setValoracion(5);

        // 2. Comportamiento del Mock
        // Cuando el repo guarde cualquier Review, devuelve la misma instancia
        when(reviewRepository.save(any(Review.class))).thenReturn(reviewInput);

        // 3. Ejecución
        Review resultado = reviewService.save(reviewInput);

        // 4. Verificaciones
        assertNotNull(resultado, "El resultado no debe ser nulo");
        assertEquals("Muy buena atención", resultado.getDescripcion());
        assertEquals(5, resultado.getValoracion());

        // Verificar que el repositorio se llamó exactamente 1 vez
        verify(reviewRepository, times(1)).save(reviewInput);
    }

    @Test
    void testBuscarReviewPorId() {
        // 1. Datos de prueba
        Long id = 10L;
        Review reviewMock = new Review();
        reviewMock.setId(id);
        reviewMock.setDescripcion("Correcto");

        // 2. Comportamiento del Mock (Simulamos que la BD encuentra el registro)
        when(reviewRepository.findById(id)).thenReturn(Optional.of(reviewMock));

        // 3. Ejecución
        Review resultado = reviewService.findById(id);

        // 4. Verificaciones
        assertNotNull(resultado);
        assertEquals(10L, resultado.getId());
        assertEquals("Correcto", resultado.getDescripcion());
        
        verify(reviewRepository).findById(id);
    }

    /*@Test
    void testBuscarReviewPorId_NoExiste() {
        // Escenario Negativo: El ID no existe en base de datos
        Long id = 99L;

        when(reviewRepository.findById(id)).thenReturn(Optional.empty());

        // Verificamos que lance excepción (ajusta RuntimeException a la que uses tú)
        assertThrows(RuntimeException.class, () -> {
            reviewService.findById(id);
        });

        verify(reviewRepository).findById(id);
    }*/
}

