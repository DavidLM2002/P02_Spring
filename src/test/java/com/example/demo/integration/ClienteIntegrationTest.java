package com.example.demo.integration;

import com.example.demo.entity.Cliente;
import com.example.demo.repository.ClienteRepository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ClienteIntegrationTest {

    @Autowired
    private ClienteRepository clienteRepository;

    @Test
    void testListarClientes() {

        // 1. Crear un cliente en la BD H2
        Cliente cliente = new Cliente();
        cliente.setNombre("Cliente Listar");
        cliente.setEdad(30);
        cliente.setGenero("Hombre");
        cliente.setIntolerancia(false);

        clienteRepository.save(cliente);

        // 2. Listar clientes
        List<Cliente> lista = clienteRepository.findAll();

        // VALIDACIONES 
        assertFalse(lista.isEmpty(), "La lista no debería estar vacía");
        assertTrue(lista.size() >= 1, "Debe haber al menos un cliente");

        boolean existe = lista.stream()
                .anyMatch(c -> c.getNombre().equals("Cliente Listar") && c.getEdad() == 30);

        assertTrue(existe, "El cliente creado debe aparecer en la lista");
    }

    @Test
    void testObtenerClientePorId() {

        // 1. Crear cliente
        Cliente cliente = new Cliente();
        cliente.setNombre("Cliente ID Dinamico");
        cliente.setEdad(25);
        cliente.setGenero("Mujer");
        cliente.setIntolerancia(true);

        Cliente guardado = clienteRepository.save(cliente);

        Long idCreado = guardado.getId();
        assertNotNull(idCreado, "El ID generado no debe ser null");

        // 2. Obtener cliente por ID
        Cliente obtenido = clienteRepository.findById(idCreado).orElse(null);

        // VALIDACIONES 
        assertNotNull(obtenido, "El cliente obtenido no debe ser null");
        assertEquals(idCreado, obtenido.getId(), "El ID debe coincidir");
        assertEquals("Cliente ID Dinamico", obtenido.getNombre(), "El nombre debe coincidir");
        assertEquals(25, obtenido.getEdad(), "La edad debe coincidir");
    }
}
