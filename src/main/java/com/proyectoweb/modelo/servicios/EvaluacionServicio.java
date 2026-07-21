package com.proyectoweb.modelo.servicios;

import java.util.List;

import com.proyectoweb.modelo.dao.ContenidoDAO;
import com.proyectoweb.modelo.dao.ContenidoDAOImpl;
import com.proyectoweb.modelo.dao.EvaluacionDAO;
import com.proyectoweb.modelo.dao.EvaluacionDAOImpl;
import com.proyectoweb.modelo.entities.ElementoContenido;
import com.proyectoweb.modelo.entities.Evaluacion;
import com.proyectoweb.modelo.entities.Usuario;

/**
 * Lógica de negocio de evaluaciones (CU05): decide crear o actualizar
 * y mantiene consistente la calificación promedio del elemento.
 */
public class EvaluacionServicio {

    private EvaluacionDAO evaluacionDAO = new EvaluacionDAOImpl();
    private ContenidoDAO contenidoDAO = new ContenidoDAOImpl();

    // CU05-A: evaluación previa del usuario sobre el elemento (o null)
    public Evaluacion obtenerEvaluacionPrevia(Usuario usuario, int elementoId) {
        return evaluacionDAO.buscarPorUsuarioYElemento(usuario.getId(), elementoId);
    }

    /**
     * CU05-B: registra o actualiza la evaluación y recalcula el promedio.
     * Devuelve el nuevo promedio del elemento.
     */
    public double registrarOActualizar(Usuario usuario, int elementoId, int calificacion, String resena) {
        Evaluacion previa = evaluacionDAO.buscarPorUsuarioYElemento(usuario.getId(), elementoId);
        if (previa == null) {
            ElementoContenido elemento = contenidoDAO.buscarPorId(elementoId);
            evaluacionDAO.guardar(new Evaluacion(usuario, elemento, calificacion, resena));
        } else {
            previa.setCalificacion(calificacion);
            previa.setResena(resena);
            evaluacionDAO.actualizar(previa);
        }
        double nuevoPromedio = calcularCalificacionPromedio(elementoId);
        contenidoDAO.actualizarCalificacionPromedio(elementoId, nuevoPromedio);
        return nuevoPromedio;
    }

    private double calcularCalificacionPromedio(int elementoId) {
        List<Integer> calificaciones = evaluacionDAO.obtenerCalificacionesPorElemento(elementoId);
        if (calificaciones.isEmpty()) {
            return 0;
        }
        double suma = 0;
        for (Integer calificacion : calificaciones) {
            suma += calificacion;
        }
        return Math.round((suma / calificaciones.size()) * 10.0) / 10.0;
    }

    // Detalle del elemento: reseñas publicadas
    public List<Evaluacion> listarPorElemento(int elementoId) {
        return evaluacionDAO.listarPorElemento(elementoId);
    }
}
