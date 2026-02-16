package com.example.demo.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Cliente;
import com.example.demo.repository.ClienteRepository;


@Service
public class ClienteService {

    @Autowired
    private ClienteRepository repo;

    public List<Cliente> findAll() { return repo.findAll(); }
    public Cliente findById(Long id) { return repo.findById(id).orElse(null); }
    public Cliente save(Cliente c) { return repo.save(c); }
    public void delete(Long id) { repo.deleteById(id); }
}

