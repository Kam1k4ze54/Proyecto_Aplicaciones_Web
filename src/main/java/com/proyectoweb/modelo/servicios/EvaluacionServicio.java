package com.proyectoweb.modelo.servicios;

import java.util.List;

import com.proyectoweb.modelo.dao.ContenidoDAO;
import com.proyectoweb.modelo.dao.ContenidoDAOImpl;
import com.proyectoweb.modelo.dao.EvaluacionDAO;
import com.proyectoweb.modelo.dao.EvaluacionDAOImpl;
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
     * CU05-B: registra o actualiza la evaluación, recalcula el promedio
     * y lo persiste en el elemento. Devuelve la evaluación registrada
     * (su elemento queda con el nuevo promedio).
     */
    public Evaluacion registrarOActualizar(Evaluacion e) {
        int elementoId = e.getElemento().getId();
        Evaluacion previa = evaluacionDAO.buscarPorUsuarioYElemento(e.getUsuario().getId(), elementoId);

        Evaluacion resultado;
        if (previa == null) {
            resultado = evaluacionDAO.guardar(e);
        } else {
            previa.setCalificacion(e.getCalificacion());
            previa.setResena(e.getResena());
            resultado = evaluacionDAO.actualizar(previa);
        }

        double nuevoPromedio = calcularCalificacionPromedio(elementoId);
        contenidoDAO.actualizarCalificacionPromedio(elementoId, nuevoPromedio);
        resultado.getElemento().setCalificacionPromedio(nuevoPromedio);
        return resultado;
    }

    // CU05-B: promedio a partir de todas las calificaciones del elemento
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
