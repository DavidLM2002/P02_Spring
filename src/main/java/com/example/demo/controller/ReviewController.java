package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.example.demo.entity.Review;
import com.example.demo.service.ClienteService;
import com.example.demo.service.ReviewService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/reviews")
public class ReviewController {

    @Autowired
    private ReviewService service;

    @Autowired
    private ClienteService clienteService;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("reviews", service.findAll());
        return "reviews/lista";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("review", new Review());
        model.addAttribute("clientes", clienteService.findAll());
        return "reviews/form";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Review review) {
        service.save(review);
        return "redirect:/reviews";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        model.addAttribute("review", service.findById(id));
        model.addAttribute("clientes", clienteService.findAll());
        return "reviews/form";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        service.delete(id);
        return "redirect:/reviews";
    }
}

