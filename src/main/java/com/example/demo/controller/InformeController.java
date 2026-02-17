package com.example.demo.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.dto.ClienteDTO;
import com.example.demo.dto.ReviewDTO;
import com.example.demo.entity.Cliente;
import com.example.demo.entity.Review;
import com.example.demo.service.ClienteService;
import com.example.demo.service.ReviewService;

import tools.jackson.databind.ObjectMapper;

/**
 * Esta es la clase principal de mi aplicación.
 */
@Controller
@RequestMapping("/informes")
public class InformeController {

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private ObjectMapper mapper;

    @GetMapping
    public String dashboard(Model model) throws Exception {

        List<ClienteDTO> clientes = clienteService.findAll()
                .stream()
                .map(c -> new ClienteDTO(
                        c.getId(),
                        c.getNombre(),
                        c.getEdad(),
                        c.getGenero(),
                        c.isIntolerancia()
                ))
                .collect(Collectors.toList());

        List<ReviewDTO> reviews = reviewService.findAll()
                .stream()
                .map(r -> new ReviewDTO(
                        r.getId(),
                        r.getDescripcion(),
                        r.getValoracion(),
                        r.getCliente().getId()
                ))
                .collect(Collectors.toList());

        model.addAttribute("clientesJson", mapper.writeValueAsString(clientes));
        model.addAttribute("reviewsJson", mapper.writeValueAsString(reviews));

        return "informes/dashboard";
    }
}



