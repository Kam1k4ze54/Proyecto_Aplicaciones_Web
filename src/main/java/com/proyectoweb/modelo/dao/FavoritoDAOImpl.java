package com.proyectoweb.modelo.dao;

import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import com.proyectoweb.modelo.entities.ElementoContenido;
import com.proyectoweb.modelo.entities.Favorito;
import com.proyectoweb.modelo.entities.Usuario;

public class FavoritoDAOImpl implements FavoritoDAO {

    private EntityManager em = JPAUtil.crearEntityManager();

    @Override
    public boolean existe(int idUsuario, int elementoId) {
        Long total = em.createQuery(
                "SELECT COUNT(f) FROM Favorito f WHERE f.usuario.id = :idUsuario AND f.elemento.id = :elementoId",
                Long.class)
                .setParameter("idUsuario", idUsuario)
                .setParameter("elementoId", elementoId)
                .getSingleResult();
        return total > 0;
    }

    @Override
    public boolean guardar(int idUsuario, int elementoId) {
        try {
            em.getTransaction().begin();
            Usuario usuario = em.find(Usuario.class, idUsuario);
            ElementoContenido elemento = em.find(ElementoContenido.class, elementoId);
            em.persist(new Favorito(usuario, elemento));
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
    public boolean eliminar(int idUsuario, int elementoId) {
        try {
            em.getTransaction().begin();
            int eliminados = em.createQuery(
                    "DELETE FROM Favorito f WHERE f.usuario.id = :idUsuario AND f.elemento.id = :elementoId")
                    .setParameter("idUsuario", idUsuario)
                    .setParameter("elementoId", elementoId)
                    .executeUpdate();
            em.getTransaction().commit();
            em.clear();
            return eliminados > 0;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            return false;
        }
    }

    @Override
    public List<ElementoContenido> listarPorUsuario(int idUsuario) {
        TypedQuery<ElementoContenido> query = em.createQuery(
                "SELECT f.elemento FROM Favorito f WHERE f.usuario.id = :idUsuario ORDER BY f.id DESC",
                ElementoContenido.class);
        query.setParameter("idUsuario", idUsuario);
        return query.getResultList();
    }

    @Override
    public int contarPorElemento(int elementoId) {
        Long total = em.createQuery(
                "SELECT COUNT(f) FROM Favorito f WHERE f.elemento.id = :elementoId", Long.class)
                .setParameter("elementoId", elementoId)
                .getSingleResult();
        return total.intValue();
    }

    @Override
    public void eliminarPorElemento(int elementoId) {
        em.getTransaction().begin();
        em.createQuery("DELETE FROM Favorito f WHERE f.elemento.id = :elementoId")
                .setParameter("elementoId", elementoId)
                .executeUpdate();
        em.getTransaction().commit();
    }
}
