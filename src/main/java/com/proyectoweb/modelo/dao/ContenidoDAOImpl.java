package com.proyectoweb.modelo.dao;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import com.proyectoweb.modelo.entities.Categoria;
import com.proyectoweb.modelo.entities.ElementoContenido;
import com.proyectoweb.modelo.entities.EstablecimientoGastronomico;
import com.proyectoweb.modelo.entities.Evento;
import com.proyectoweb.modelo.entities.LugarTuristico;

public class ContenidoDAOImpl implements ContenidoDAO {

    private EntityManager em = JPAUtil.crearEntityManager();

    // Traduce el tipo (String de URL/vista) a la subclase de la jerarquía
    private Class<? extends ElementoContenido> claseDe(String tipo) {
        switch (tipo) {
            case "LugarTuristico":
                return LugarTuristico.class;
            case "EstablecimientoGastronomico":
                return EstablecimientoGastronomico.class;
            case "Evento":
                return Evento.class;
            default:
                throw new IllegalArgumentException("Tipo de contenido desconocido: " + tipo);
        }
    }

    @Override
    public List<ElementoContenido> obtenerPorCategoriasLimitado(List<Categoria> categorias, String tipo, int limite) {
        if (categorias == null || categorias.isEmpty()) {
            return new ArrayList<>();
        }
        TypedQuery<ElementoContenido> query = em.createQuery(
                "SELECT e FROM ElementoContenido e WHERE TYPE(e) = :clase AND e.activo = true "
                        + "AND e.categoria IN :categorias ORDER BY e.destacado DESC, e.calificacionPromedio DESC",
                ElementoContenido.class);
        query.setParameter("clase", claseDe(tipo));
        query.setParameter("categorias", categorias);
        query.setMaxResults(limite);
        return query.getResultList();
    }

    @Override
    public List<ElementoContenido> obtenerDestacadosPorTipo(String tipo, int limite) {
        TypedQuery<ElementoContenido> query = em.createQuery(
                "SELECT e FROM ElementoContenido e WHERE TYPE(e) = :clase AND e.activo = true "
                        + "AND e.destacado = true ORDER BY e.calificacionPromedio DESC",
                ElementoContenido.class);
        query.setParameter("clase", claseDe(tipo));
        query.setMaxResults(limite);
        return query.getResultList();
    }

    @Override
    public List<ElementoContenido> buscarPorNombre(String termino) {
        TypedQuery<ElementoContenido> query = em.createQuery(
                "SELECT e FROM ElementoContenido e WHERE e.activo = true "
                        + "AND LOWER(e.nombre) LIKE :termino ORDER BY e.nombre",
                ElementoContenido.class);
        query.setParameter("termino", "%" + termino.toLowerCase() + "%");
        return query.getResultList();
    }

    @Override
    public List<ElementoContenido> listarTodosPorTipo(String tipo, boolean soloActivos) {
        String jpql = "SELECT e FROM ElementoContenido e WHERE TYPE(e) = :clase"
                + (soloActivos ? " AND e.activo = true" : "")
                + " ORDER BY e.nombre";
        TypedQuery<ElementoContenido> query = em.createQuery(jpql, ElementoContenido.class);
        query.setParameter("clase", claseDe(tipo));
        return query.getResultList();
    }

    @Override
    public ElementoContenido buscarPorId(int id) {
        return em.find(ElementoContenido.class, id);
    }

    @Override
    public ElementoContenido guardar(ElementoContenido elemento) {
        em.getTransaction().begin();
        em.persist(elemento);
        em.getTransaction().commit();
        return elemento;
    }

    @Override
    public ElementoContenido actualizar(ElementoContenido elemento) {
        em.getTransaction().begin();
        ElementoContenido actualizado = em.merge(elemento);
        em.getTransaction().commit();
        return actualizado;
    }

    @Override
    public boolean eliminar(int id) {
        try {
            em.getTransaction().begin();
            ElementoContenido elemento = em.find(ElementoContenido.class, id);
            if (elemento != null) {
                em.remove(elemento);
            }
            em.getTransaction().commit();
            return elemento != null;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            return false;
        }
    }

    @Override
    public boolean actualizarDestacado(int id, boolean nuevoEstado) {
        try {
            em.getTransaction().begin();
            ElementoContenido elemento = em.find(ElementoContenido.class, id);
            if (elemento != null) {
                elemento.setDestacado(nuevoEstado);
            }
            em.getTransaction().commit();
            return elemento != null;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            return false;
        }
    }

    @Override
    public void actualizarCalificacionPromedio(int elementoId, double promedio) {
        em.getTransaction().begin();
        ElementoContenido elemento = em.find(ElementoContenido.class, elementoId);
        if (elemento != null) {
            elemento.setCalificacionPromedio(promedio);
        }
        em.getTransaction().commit();
    }
}
