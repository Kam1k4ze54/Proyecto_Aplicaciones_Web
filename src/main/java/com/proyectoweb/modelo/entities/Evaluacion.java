package com.proyectoweb.modelo.entities;

import java.io.Serializable;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * Calificación (1-5) y reseña de un usuario sobre un elemento (CU05).
 */
@Entity
@Table(name = "evaluacion")
public class Evaluacion implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "elemento_id", nullable = false)
    private ElementoContenido elemento;

    @Column(name = "calificacion")
    private int calificacion;

    @Column(name = "resena", length = 2000)
    private String resena;

    @Column(name = "fecha")
    private LocalDateTime fecha;

    public Evaluacion() {
    }

    public Evaluacion(Usuario usuario, ElementoContenido elemento, int calificacion, String resena) {
        this.usuario = usuario;
        this.elemento = elemento;
        this.calificacion = calificacion;
        this.resena = resena;
        this.fecha = LocalDateTime.now();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public ElementoContenido getElemento() {
        return elemento;
    }

    public void setElemento(ElementoContenido elemento) {
        this.elemento = elemento;
    }

    public int getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(int calificacion) {
        this.calificacion = calificacion;
    }

    public String getResena() {
        return resena;
    }

    public void setResena(String resena) {
        this.resena = resena;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }
}
