package com.example.demo.unit;

import com.example.demo.entity.Cliente;
import com.example.demo.repository.ClienteRepository;
import com.example.demo.service.ClienteService;
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
class ClienteServiceTest {

    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private ClienteService clienteService; 

    @Test
    void testGuardarCliente() {
        // 1. Datos de prueba
        Cliente clienteInput = new Cliente();
        clienteInput.setNombre("Ana");

        // 2. Simular comportamiento del repositorio (Mock)
        // Cuando llamen a save con CUALQUIER cliente, devuelve el mismo cliente
        when(clienteRepository.save(any(Cliente.class))).thenReturn(clienteInput);

        // 3. Ejecutar el servicio
        Cliente resultado = clienteService.save(clienteInput);

        // 4. Verificaciones (Asserts)
        assertNotNull(resultado, "El resultado no debería ser nulo");
        assertEquals("Ana", resultado.getNombre(), "El nombre no coincide");

        // 5. Verificar que el repositorio fue llamado 1 vez
        verify(clienteRepository, times(1)).save(clienteInput);
    }

    @Test
    void testBuscarPorId() {
        // 1. Datos de prueba
        Long id = 1L;
        Cliente clienteMock = new Cliente();
        clienteMock.setId(id);
        clienteMock.setNombre("Luis");

        // 2. Simular comportamiento: El repositorio encuentra el cliente (Optional)
        when(clienteRepository.findById(id)).thenReturn(Optional.of(clienteMock));

        // 3. Ejecutar servicio
        Cliente resultado = clienteService.findById(id);

        // 4. Verificaciones
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Luis", resultado.getNombre());
        
        // Verificamos que se llamó al repositorio
        verify(clienteRepository).findById(id);
    }

    // Test para cuando NO existe el cliente 
    @Test
    void testBuscarPorId_NoExiste() {
        Long id = 99L;

        // Simulamos que el repositorio devuelve Empty
        when(clienteRepository.findById(id)).thenReturn(Optional.empty());

        // Verificamos que el servicio lanza una excepción 
        assertThrows(RuntimeException.class, () -> {
            clienteService.findById(id);
        });
        
        verify(clienteRepository).findById(id);
    }
}

