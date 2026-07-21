package com.proyectoweb.modelo.dao;

import java.util.List;

import com.proyectoweb.modelo.entities.Categoria;

public interface CategoriaDAO {
    List<Categoria> obtenerCategorias(); // CU paso 6 (catálogo de categorías)

    List<Categoria> obtenerCategoriasPorTipo(String tipo); // CU07 (categorías aplicables al tipo de contenido)

    Categoria buscarPorId(int id); // CU07 (categoría seleccionada en el formulario)
}
