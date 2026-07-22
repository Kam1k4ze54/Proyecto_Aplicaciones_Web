package com.proyectoweb.dao;

import java.util.List;

import com.proyectoweb.modelo.Usuario;
import com.proyectoweb.util.JPAUtil;

import jakarta.persistence.EntityManager;

public class UsuarioDAO {

    public void guardar(Usuario usuario) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(usuario);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    public List<Usuario> listarTodos() {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            return em.createQuery("SELECT u FROM Usuario u", Usuario.class).getResultList();
        } finally {
            em.close();
        }
    }
}
