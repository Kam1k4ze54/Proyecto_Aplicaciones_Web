package com.proyectoweb.modelo.servicios;

import java.util.List;

import com.proyectoweb.modelo.dao.CategoriaDAO;
import com.proyectoweb.modelo.dao.CategoriaDAOImpl;
import com.proyectoweb.modelo.entities.Categoria;

/**
 * Catálogo de categorías (CU01, CU03, CU07).
 */
public class CategoriaServicio {

    private CategoriaDAO categoriaDAO = new CategoriaDAOImpl();

    public List<Categoria> obtenerCategorias() {
        return categoriaDAO.obtenerCategorias();
    }

    // CU07: categoría elegida en el formulario de contenido
    public Categoria buscarPorId(int id) {
        return categoriaDAO.buscarPorId(id);
    }

    // CU07: categorías aplicables según el tipo de contenido gestionado
    public List<Categoria> obtenerCategoriasPorTipo(String tipoContenido) {
        return categoriaDAO.obtenerCategoriasPorTipo(tipoCategoriaDe(tipoContenido));
    }

    // Mapea el tipo de contenido al tipo de categoría del catálogo
    private String tipoCategoriaDe(String tipoContenido) {
        switch (tipoContenido) {
            case "LugarTuristico":
                return "Turística";
            case "EstablecimientoGastronomico":
                return "Gastronómica";
            case "Evento":
                return "Cultural";
            default:
                throw new IllegalArgumentException("Tipo de contenido desconocido: " + tipoContenido);
        }
    }
}
