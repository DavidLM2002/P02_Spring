package com.example.demo.entity;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "cliente")
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "edad")
    private int edad;

    @Column(name = "genero")
    private String genero;

    @Column(name = "intolerancia")
    private boolean intolerancia;

    @Column(name = "detalle_intolerancia")
    private String detalleIntolerancia;

    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL)
    private List<Review> reviews;
    
 // 🔽 GETTERS Y SETTERS

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public boolean isIntolerancia() {
        return intolerancia;
    }

    public void setIntolerancia(boolean intolerancia) {
        this.intolerancia = intolerancia;
    }

    public String getDetalleIntolerancia() {
        return detalleIntolerancia;
    }

    public void setDetalleIntolerancia(String detalleIntolerancia) {
        this.detalleIntolerancia = detalleIntolerancia;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }
}

