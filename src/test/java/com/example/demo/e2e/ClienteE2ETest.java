package com.example.demo.e2e;

import com.example.demo.entity.Cliente;
import com.jayway.jsonpath.JsonPath;

import tools.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional // IMPORTANTE: Mantiene la BD limpia tras cada ejecución
class ClienteE2ETest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper; // Serializador de JSON oficial de Spring

    @Test
    void testCicloDeVidaCompleto_Cliente() throws Exception {
        // ==========================================
        // PASO 1: CREAR EL CLIENTE (POST)
        // ==========================================
        System.out.println(">>> 1. CREANDO CLIENTE...");

        Cliente nuevoCliente = new Cliente();
        nuevoCliente.setNombre("Cliente E2E Ciclo");
        nuevoCliente.setEdad(35);
        nuevoCliente.setGenero("Mujer");
        // Asegúrate de que tu entidad acepte boolean aquí, si es int pon 0 o 1
        nuevoCliente.setIntolerancia(false); 

        // Usamos ObjectMapper para convertir el Objeto Java a JSON String automáticamente
        String jsonRequest = objectMapper.writeValueAsString(nuevoCliente);

        MvcResult createResult = mockMvc.perform(post("/api/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre", is("Cliente E2E Ciclo")))
                .andExpect(jsonPath("$.id", notNullValue())) 
                .andReturn();

        // Capturamos el ID generado por la Base de Datos
        Integer idGenerado = JsonPath.read(createResult.getResponse().getContentAsString(), "$.id");
        System.out.println(">>> ID GENERADO: " + idGenerado);


        // ==========================================
        // PASO 2: CONSULTAR POR ID (GET /id)
        // ==========================================
        System.out.println(">>> 2. CONSULTANDO POR ID...");
        
        mockMvc.perform(get("/api/clientes/" + idGenerado))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.nombre", is("Cliente E2E Ciclo")))
                .andExpect(jsonPath("$.edad", is(35)));


        // ==========================================
        // PASO 3: VERIFICAR EN EL LISTADO (GET /)
        // ==========================================
        System.out.println(">>> 3. VERIFICANDO EN LISTADO GENERAL...");

        mockMvc.perform(get("/api/clientes"))
                .andExpect(status().isOk())
                // Verificamos que existe un elemento en el array con ese ID
                .andExpect(jsonPath("$[?(@.id == " + idGenerado + ")]").exists());


        // ==========================================
        // PASO 4: ELIMINAR EL CLIENTE (DELETE /id)
        // ==========================================
        System.out.println(">>> 4. ELIMINANDO CLIENTE...");

        mockMvc.perform(delete("/api/clientes/" + idGenerado))
                .andExpect(status().isOk());


        // ==========================================
        // PASO 5: CONFIRMAR ELIMINACIÓN
        // ==========================================
        System.out.println(">>> 5. CONFIRMANDO QUE YA NO EXISTE...");

        // Verificamos que al listar todos, nuestro ID ya NO aparece
        mockMvc.perform(get("/api/clientes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[?(@.id == " + idGenerado + ")]").doesNotExist());
    }
}


