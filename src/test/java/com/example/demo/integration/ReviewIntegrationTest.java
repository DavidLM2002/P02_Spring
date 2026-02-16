package com.example.demo.integration;

import com.example.demo.entity.Cliente;
import com.example.demo.entity.Review;
import com.example.demo.repository.ClienteRepository;
import com.example.demo.repository.ReviewRepository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional 
class ReviewIntegrationTest {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Test
    void testCrearReview_ConAssertions() {

        // 1. Crear cliente
        Cliente cliente = new Cliente();
        cliente.setNombre("Cliente Assert");
        cliente.setEdad(25);
        cliente.setGenero("Mujer");
        cliente.setIntolerancia(false);

        Cliente clienteGuardado = clienteRepository.save(cliente);

        // 2. Crear review asociada
        Review review = new Review();
        review.setDescripcion("Excelente servicio");
        review.setValoracion(5);
        review.setCliente(clienteGuardado);

        Review reviewCreada = reviewRepository.save(review);

        // ASSERTIONS
        assertNotNull(reviewCreada.getId(), "El ID de la review no debería ser nulo");
        assertEquals("Excelente servicio", reviewCreada.getDescripcion());
        assertEquals(5, reviewCreada.getValoracion());
        assertEquals(clienteGuardado.getId(), reviewCreada.getCliente().getId());
        assertTrue(reviewCreada.getValoracion() > 0);
    }

    @Test
    void testListarReviews_ConAssertions() {

        // 1. Crear cliente
        Cliente cliente = new Cliente();
        cliente.setNombre("Cliente Lista");
        cliente.setEdad(50);
        cliente.setGenero("Hombre");
        cliente.setIntolerancia(false);

        Cliente clienteGuardado = clienteRepository.save(cliente);

        // 2. Crear review
        Review review = new Review();
        review.setDescripcion("Review Lista");
        review.setValoracion(3);
        review.setCliente(clienteGuardado);

        reviewRepository.save(review);

        // 3. Listar reviews
        List<Review> lista = reviewRepository.findAll();

        // --- ASSERTIONS ---
        assertFalse(lista.isEmpty(), "La lista no debería estar vacía");
        assertTrue(lista.size() >= 1);

        boolean existe = lista.stream()
                .anyMatch(r -> r.getDescripcion().equals("Review Lista") &&
                               r.getValoracion() == 3);

        assertTrue(existe, "La review creada debe aparecer en la lista");
    }
}
