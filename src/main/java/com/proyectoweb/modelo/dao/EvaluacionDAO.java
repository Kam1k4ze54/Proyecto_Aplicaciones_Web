package com.proyectoweb.modelo.dao;

import java.util.List;

import com.proyectoweb.modelo.entities.Evaluacion;

public interface EvaluacionDAO {
    Evaluacion buscarPorUsuarioYElemento(int idUsuario, int elementoId); // CU05 (evaluación previa)

    Evaluacion guardar(Evaluacion evaluacion); // CU05 (primera evaluación)

    Evaluacion actualizar(Evaluacion evaluacion); // CU05 (modificar evaluación previa)

    List<Integer> obtenerCalificacionesPorElemento(int elementoId); // CU05 (recalcular promedio)

    List<Evaluacion> listarPorElemento(int elementoId); // Detalle: reseñas publicadas

    int contarPorElemento(int elementoId); // CU07-D (dependencias al eliminar)

    void eliminarPorElemento(int elementoId); // CU07-D (limpieza al eliminar contenido)
}
