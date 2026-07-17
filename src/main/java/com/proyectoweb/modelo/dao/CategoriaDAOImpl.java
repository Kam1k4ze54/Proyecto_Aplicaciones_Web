package com.proyectoweb.modelo.dao;

import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Persistence;

import com.proyectoweb.modelo.entities.Categoria;

public class CategoriaDAOImpl implements CategoriaDAO {

    private EntityManager em = Persistence
            .createEntityManagerFactory("ProyectoWebPU")
            .createEntityManager();

    @Override
    public List<Categoria> obtenerCategorias() {
        // CU paso 6: trae todo el catálogo de categorías (turísticas y gastronómicas)
        return em.createQuery("SELECT c FROM Categoria c", Categoria.class).getResultList();
    }
}
