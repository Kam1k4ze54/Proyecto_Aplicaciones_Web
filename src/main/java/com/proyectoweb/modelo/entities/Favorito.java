package com.proyectoweb.modelo.entities;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * Elemento guardado como favorito por un usuario (CU06).
 * Regla de negocio: los eventos no son favoriteables.
 */
@Entity
@Table(name = "favorito")
public class Favorito implements Serializable {
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

    public Favorito() {
    }

    public Favorito(Usuario usuario, ElementoContenido elemento) {
        this.usuario = usuario;
        this.elemento = elemento;
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
}
