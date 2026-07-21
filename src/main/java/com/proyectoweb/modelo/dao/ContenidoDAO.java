package com.proyectoweb.modelo.dao;

import java.util.List;

import com.proyectoweb.modelo.entities.Categoria;
import com.proyectoweb.modelo.entities.ElementoContenido;

public interface ContenidoDAO {
    // CU04: primeros N elementos activos de un tipo cuya categoría está en las preferencias
    List<ElementoContenido> obtenerPorCategoriasLimitado(List<Categoria> categorias, String tipo, int limite);

    // CU04 alterno 7: primeros N destacados activos de un tipo (fallback)
    List<ElementoContenido> obtenerDestacadosPorTipo(String tipo, int limite);

    // CU04 alterno 5: búsqueda por nombre sobre los tres tipos
    List<ElementoContenido> buscarPorNombre(String termino);

    // CU04 alterno 6 / CU07: lista completa de un tipo
    List<ElementoContenido> listarTodosPorTipo(String tipo, boolean soloActivos);

    ElementoContenido buscarPorId(int id); // CU04 / CU07

    ElementoContenido guardar(ElementoContenido elemento); // CU07-B

    ElementoContenido actualizar(ElementoContenido elemento); // CU07-C

    boolean eliminar(int id); // CU07-D

    boolean actualizarDestacado(int id, boolean nuevoEstado); // CU07-E

    void actualizarCalificacionPromedio(int elementoId, double promedio); // CU05
}
