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

import com.example.demo.entity.Review;
import com.example.demo.service.ReviewService;

@RestController
@RequestMapping("/api/reviews")
public class ReviewRestController {

    @Autowired
    private ReviewService reviewService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Review> listar() {
        return reviewService.findAll();
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Review obtener(@PathVariable Long id) {
        return reviewService.findById(id);
    }


    @PostMapping
    public Review crear(@RequestBody Review review) {
        return reviewService.save(review);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        reviewService.delete(id);
    }
}

