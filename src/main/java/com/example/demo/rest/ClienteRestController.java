package com.example.demo.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.demo.entity.Cliente;
import com.example.demo.service.ClienteService;

@RestController
@RequestMapping(value = "/api/clientes", produces = "application/json")
public class ClienteRestController {

    @Autowired
    private ClienteService clienteService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Cliente> listar() {
    	System.out.println(">>> ENTRANDO EN /api/clientes");
        return clienteService.findAll();
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Cliente obtener(@PathVariable Long id) {
        return clienteService.findById(id);
    }

    @PostMapping
    public Cliente crear(@RequestBody Cliente cliente) {
        return clienteService.save(cliente);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        clienteService.delete(id);
    }
}

