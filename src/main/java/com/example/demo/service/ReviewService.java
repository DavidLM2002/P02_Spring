package com.example.demo.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Review;
import com.example.demo.repository.ReviewRepository;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository repo;

    public List<Review> findAll() { return repo.findAll(); }
    public Review findById(Long id) { return repo.findById(id).orElse(null); }
    public Review save(Review r) { return repo.save(r); }
    public void delete(Long id) { repo.deleteById(id); }
}

