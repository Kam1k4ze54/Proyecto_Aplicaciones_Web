package com.proyectoweb.modelo.dao;

import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import com.proyectoweb.modelo.entities.Evaluacion;

public class EvaluacionDAOImpl implements EvaluacionDAO {

    private EntityManager em = JPAUtil.crearEntityManager();

    @Override
    public Evaluacion buscarPorUsuarioYElemento(int idUsuario, int elementoId) {
        TypedQuery<Evaluacion> query = em.createQuery(
                "SELECT ev FROM Evaluacion ev WHERE ev.usuario.id = :idUsuario AND ev.elemento.id = :elementoId",
                Evaluacion.class);
        query.setParameter("idUsuario", idUsuario);
        query.setParameter("elementoId", elementoId);
        List<Evaluacion> resultado = query.getResultList();
        return resultado.isEmpty() ? null : resultado.get(0);
    }

    @Override
    public Evaluacion guardar(Evaluacion evaluacion) {
        em.getTransaction().begin();
        em.persist(evaluacion);
        em.getTransaction().commit();
        return evaluacion;
    }

    @Override
    public Evaluacion actualizar(Evaluacion evaluacion) {
        em.getTransaction().begin();
        Evaluacion actualizada = em.merge(evaluacion);
        em.getTransaction().commit();
        return actualizada;
    }

    @Override
    public List<Integer> obtenerCalificacionesPorElemento(int elementoId) {
        TypedQuery<Integer> query = em.createQuery(
                "SELECT ev.calificacion FROM Evaluacion ev WHERE ev.elemento.id = :elementoId", Integer.class);
        query.setParameter("elementoId", elementoId);
        return query.getResultList();
    }

    @Override
    public List<Evaluacion> listarPorElemento(int elementoId) {
        TypedQuery<Evaluacion> query = em.createQuery(
                "SELECT ev FROM Evaluacion ev WHERE ev.elemento.id = :elementoId ORDER BY ev.fecha DESC",
                Evaluacion.class);
        query.setParameter("elementoId", elementoId);
        return query.getResultList();
    }

    @Override
    public int contarPorElemento(int elementoId) {
        Long total = em.createQuery(
                "SELECT COUNT(ev) FROM Evaluacion ev WHERE ev.elemento.id = :elementoId", Long.class)
                .setParameter("elementoId", elementoId)
                .getSingleResult();
        return total.intValue();
    }

    @Override
    public void eliminarPorElemento(int elementoId) {
        em.getTransaction().begin();
        em.createQuery("DELETE FROM Evaluacion ev WHERE ev.elemento.id = :elementoId")
                .setParameter("elementoId", elementoId)
                .executeUpdate();
        em.getTransaction().commit();
    }
}
