package com.proyectoweb.modelo.dao;

import java.util.List;
import java.util.Map;

import com.proyectoweb.modelo.entities.Categoria;
import com.proyectoweb.modelo.entities.ElementoContenido;

public interface ContenidoDAO {
    // CU04-A: por cada tipo, los primeros N elementos activos cuya categoría
    // está en las preferencias del usuario (clave del mapa = tipo)
    Map<String, List<ElementoContenido>> obtenerPorCategoriasLimitado(List<Categoria> categorias, int limite);

    // CU04-A alterno 7: primeros N destacados activos de un tipo (fallback)
    List<ElementoContenido> obtenerDestacadosPorTipo(String tipo, int limite);

    // CU04-B: búsqueda por nombre sobre los tres tipos
    List<ElementoContenido> buscarPorNombre(String termino);

    // CU04-C / CU07-A: lista completa de un tipo (activos)
    List<ElementoContenido> listarTodosPorTipo(String tipo);

    ElementoContenido buscarPorId(int id, String tipo); // CU04-A / CU07-C

    ElementoContenido guardar(ElementoContenido elemento); // CU07-B

    ElementoContenido actualizar(ElementoContenido elemento); // CU07-C

    boolean eliminar(int id, String tipo); // CU07-D

    boolean actualizarDestacado(int id, boolean nuevoEstado); // CU07-E

    void actualizarCalificacionPromedio(int elementoId, double promedio); // CU05-B
}
