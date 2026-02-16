package com.example.demo.e2e;

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
@Transactional 
class ReviewE2ETest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testCicloVidaCompletoReview() throws Exception {
    	
        // PASO 1: CREAR EL CLIENTE (Requisito previo)
        System.out.println(">>> 1. CREANDO CLIENTE PREVIO...");
        
        String clienteJson = """
        {
            "nombre": "Cliente Para Review E2E",
            "edad": 28,
            "genero": "Mujer",
            "intolerancia": 0
        }
        """;

        MvcResult clienteResult = mockMvc.perform(post("/api/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(clienteJson))
                .andExpect(status().isOk())
                .andReturn();

        // Capturamos el ID del cliente generado por la BD
        Integer idCliente = JsonPath.read(clienteResult.getResponse().getContentAsString(), "$.id");
        System.out.println("   -> Cliente creado con ID: " + idCliente);


        // PASO 2: CREAR LA REVIEW (POST)
        System.out.println(">>> 2. CREANDO REVIEW VINCULADA...");

        // Inyectamos el ID del cliente en el JSON de la review usando .formatted()
        String reviewJson = """
        {
            "descripcion": "Review de Ciclo Completo",
            "valoracion": 5,
            "cliente": { "id": %d }
        }
        """.formatted(idCliente);

        MvcResult reviewResult = mockMvc.perform(post("/api/reviews")
                .contentType(MediaType.APPLICATION_JSON)
                .content(reviewJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.descripcion", is("Review de Ciclo Completo")))
                .andExpect(jsonPath("$.cliente.id", is(idCliente)))
                .andReturn();

        // Capturamos el ID de la review
        Integer idReview = JsonPath.read(reviewResult.getResponse().getContentAsString(), "$.id");
        System.out.println("   -> Review creada con ID: " + idReview);


        // PASO 3: CONSULTAR POR ID (GET /id)
        System.out.println(">>> 3. CONSULTANDO REVIEW POR ID...");

        mockMvc.perform(get("/api/reviews/" + idReview))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(idReview)))
                .andExpect(jsonPath("$.valoracion", is(5)));


        // PASO 4: VERIFICAR EN LISTADO GENERAL (GET /)
        System.out.println(">>> 4. VERIFICANDO EN LISTADO...");

        mockMvc.perform(get("/api/reviews"))
                .andExpect(status().isOk())
                // Verificamos que en la lista exista un elemento con nuestro ID
                .andExpect(jsonPath("$[?(@.id == " + idReview + ")]").exists())
                .andExpect(jsonPath("$[?(@.descripcion == 'Review de Ciclo Completo')]").exists());


        // PASO 5: ELIMINAR LA REVIEW (DELETE /id)
        System.out.println(">>> 5. ELIMINANDO REVIEW...");

        mockMvc.perform(delete("/api/reviews/" + idReview))
                .andExpect(status().isOk()); 


        // PASO 6: CONFIRMAR ELIMINACIÓN
        System.out.println(">>> 6. CONFIRMANDO ELIMINACIÓN...");

        // Verificamos que al listar todas, nuestra review YA NO esté
        mockMvc.perform(get("/api/reviews"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[?(@.id == " + idReview + ")]").doesNotExist());
    }
}

