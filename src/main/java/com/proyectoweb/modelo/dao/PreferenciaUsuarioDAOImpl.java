package com.proyectoweb.modelo.dao;

import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import com.proyectoweb.modelo.entities.Categoria;
import com.proyectoweb.modelo.entities.PreferenciaUsuario;
import com.proyectoweb.modelo.entities.Usuario;

public class PreferenciaUsuarioDAOImpl implements PreferenciaUsuarioDAO {

    private EntityManager em = JPAUtil.crearEntityManager();

    @Override
    public boolean guardarPreferencias(Usuario usuario, List<Integer> seleccion) {
        // CU paso 9: registra las preferencias seleccionadas del usuario recién creado
        try {
            em.getTransaction().begin();
            for (Integer idCategoria : seleccion) {
                Categoria categoria = em.find(Categoria.class, idCategoria);
                if (categoria != null) {
                    em.persist(new PreferenciaUsuario(usuario, categoria));
                }
            }
            em.getTransaction().commit();
            return true;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            return false;
        }
    }

    @Override
    public List<Categoria> obtenerPreferencias(Usuario usuario) {
        // CU03: preferencias actuales del usuario
        TypedQuery<Categoria> query = em.createQuery(
                "SELECT pu.categoria FROM PreferenciaUsuario pu WHERE pu.usuario = :usuario", Categoria.class);
        query.setParameter("usuario", usuario);
        return query.getResultList();
    }

    @Override
    public List<Categoria> obtenerCategoriasPreferidas(Usuario usuario) {
        // CU04-A: mismas categorías del usuario, nombre según el diagrama de CU04
        return obtenerPreferencias(usuario);
    }

    @Override
    public boolean actualizarPreferencias(Usuario usuario, List<Integer> seleccion) {
        // CU03: reemplaza las preferencias del usuario por la nueva selección
        try {
            em.getTransaction().begin();
            TypedQuery<PreferenciaUsuario> query = em.createQuery(
                    "SELECT pu FROM PreferenciaUsuario pu WHERE pu.usuario = :usuario", PreferenciaUsuario.class);
            query.setParameter("usuario", usuario);
            for (PreferenciaUsuario existente : query.getResultList()) {
                em.remove(existente);
            }
            for (Integer idCategoria : seleccion) {
                Categoria categoria = em.find(Categoria.class, idCategoria);
                if (categoria != null) {
                    em.persist(new PreferenciaUsuario(usuario, categoria));
                }
            }
            em.getTransaction().commit();
            return true;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            return false;
        }
    }
}
