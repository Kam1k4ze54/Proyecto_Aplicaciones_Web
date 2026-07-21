package com.proyectoweb.modelo.dao;

import java.util.List;

import com.proyectoweb.modelo.entities.ElementoContenido;

public interface FavoritoDAO {
    boolean existe(int idUsuario, int elementoId); // CU06-A (duplicado)

    boolean guardar(int idUsuario, int elementoId); // CU06-A

    boolean eliminar(int idUsuario, int elementoId); // CU06-C

    List<ElementoContenido> listarPorUsuario(int idUsuario); // CU06-B

    int contarPorElemento(int elementoId); // CU07-D (dependencias al eliminar)

    void eliminarPorElemento(int elementoId); // CU07-D (limpieza al eliminar contenido)
}
