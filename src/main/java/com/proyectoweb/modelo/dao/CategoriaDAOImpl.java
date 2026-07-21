package com.proyectoweb.modelo.dao;

import java.util.List;

import jakarta.persistence.EntityManager;

import com.proyectoweb.modelo.entities.Categoria;

public class CategoriaDAOImpl implements CategoriaDAO {

    private EntityManager em = JPAUtil.crearEntityManager();

    @Override
    public List<Categoria> obtenerCategorias() {
        // CU paso 6: trae todo el catálogo de categorías (turísticas y gastronómicas)
        return em.createQuery("SELECT c FROM Categoria c", Categoria.class).getResultList();
    }

    @Override
    public List<Categoria> obtenerCategoriasPorTipo(String tipo) {
        // CU07: categorías aplicables al tipo de contenido que se está gestionando
        return em.createQuery("SELECT c FROM Categoria c WHERE c.tipo = :tipo ORDER BY c.nombre", Categoria.class)
                .setParameter("tipo", tipo)
                .getResultList();
    }

    @Override
    public Categoria buscarPorId(int id) {
        return em.find(Categoria.class, id);
    }
}
